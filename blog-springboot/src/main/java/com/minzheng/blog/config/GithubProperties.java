package com.minzheng.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * github配置属性
 *
 * @author yezhqiu
 * @date 2021/06/14
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "github")
public class GithubProperties {

    /**
     * github appId
     */
    private String clientId;

    /**
     * github clientSecret
     */
    private String clientSecret;

    /**
     * github 回调域名
     */
    private String redirectUrl;

    /**
     * github 访问令牌地址
     */
    private String accessTokenUrl;

    /**
     * 微博用户信息地址
     */
    private String userInfoUrl;

}
