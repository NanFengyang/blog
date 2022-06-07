package com.minzheng.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 77628
 * @date 2022/2/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "github登录信息")
public class GithubLoginVO {

    @NotBlank(message = "code")
    @ApiModelProperty(name = "code", value = "qq openId", required = true, dataType = "String")
    private String code;

    @NotBlank(message = "state")
    @ApiModelProperty(name = "code", value = "qq openId", required = true, dataType = "String")
    private String state;

}
