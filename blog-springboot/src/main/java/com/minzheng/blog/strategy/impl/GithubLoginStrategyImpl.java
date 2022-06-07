package com.minzheng.blog.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.minzheng.blog.config.GithubProperties;
import com.minzheng.blog.config.WeiboConfigProperties;
import com.minzheng.blog.dto.*;
import com.minzheng.blog.enums.LoginTypeEnum;
import com.minzheng.blog.exception.BizException;
import com.minzheng.blog.vo.GithubLoginVO;
import com.minzheng.blog.vo.WeiboLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.minzheng.blog.constant.SocialLoginConst.*;
import static com.minzheng.blog.enums.StatusCodeEnum.GITHUB_LOGIN_ERROR;

/**
 * github登录策略实现
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Service("githubLoginStrategyImpl")
public class GithubLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {
    @Autowired
    private GithubProperties githubProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SocialTokenDTO getSocialToken(String data) {
        GithubLoginVO githubLoginVO = JSON.parseObject(data, GithubLoginVO.class);
        // 获取github token信息
        GithubTokenDTO githubTokenDTO = getGithubToken(githubLoginVO);
        // 返回token信息
        return SocialTokenDTO.builder()
                .accessToken(githubTokenDTO.getAccess_token())
                .loginType(LoginTypeEnum.GITHUB.getType())
                .build();
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO) {
        // 获取github用户信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","token "+socialTokenDTO.getAccessToken());
        HttpEntity<GithubUserInfoDTO> httpEntity = new HttpEntity(headers);
        GithubUserInfoDTO githubUserInfoDTO = restTemplate.exchange(githubProperties.getUserInfoUrl(), HttpMethod.GET, httpEntity,GithubUserInfoDTO.class).getBody();
        socialTokenDTO.setOpenId(githubUserInfoDTO.getLogin());
        socialTokenDTO.setLoginType(4);
        // 返回用户信息
        return SocialUserInfoDTO.builder()
                .nickname(Objects.requireNonNull(githubUserInfoDTO).getName())
                .avatar(githubUserInfoDTO.getAvatar_url())
                .build();
    }

    /**
     * 获取github token信息
     *
     * @param githubLoginVO github 登录信息
     * @return {@link GithubTokenDTO} github token
     */
    private GithubTokenDTO getGithubToken(GithubLoginVO githubLoginVO) {
        // 根据code换取git code和accessToken
        MultiValueMap<String, String> githubData = new LinkedMultiValueMap<>();
        // 定义微博token请求参数
        githubData.add(CLIENT_ID, githubProperties.getClientId());
        githubData.add(CLIENT_SECRET, githubProperties.getClientSecret());
        githubData.add(REDIRECT_URI, githubProperties.getRedirectUrl());
        githubData.add(CODE, githubLoginVO.getCode());
        githubData.add(STATE, githubLoginVO.getState());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(githubData, null);
        try {
            return restTemplate.exchange(githubProperties.getAccessTokenUrl(), HttpMethod.POST, requestEntity, GithubTokenDTO.class).getBody();
        } catch (Exception e) {
            throw new BizException(GITHUB_LOGIN_ERROR);
        }
    }

}
