package com.cqupt.readingcloud.account.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    private static final Long serialVersionUID = 1L;

    private Integer id;

    //唯一id
    private String uuid;

    //登录名
    private String loginName;

    //昵称
    private String nickName;

    //手机号
    private String phoneNumber;

    //头像
    private String headImgUrl;
}
