package com.cqupt.readingcloud.account.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户注册信息")
@Data
public class UserBO {

    @ApiModelProperty(value = "登录名(至少4个字符)", example = "yangguo")
    private String loginName;

    @ApiModelProperty(value = "密码", example = "123456")
    private String userPwd;

    @ApiModelProperty(value = "昵称", example = "杨过")
    private String nickName;

    @ApiModelProperty(value = "手机", example = "13888888888")
    private String phoneNumber;
}
