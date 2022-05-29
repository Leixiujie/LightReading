package com.cqupt.readingcloud.account.service;

import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.account.bo.UserBO;
import com.cqupt.readingcloud.account.vo.AuthVO;

/**
 * 用户服务
 */
public interface UserService {

    //注册
    Result register(UserBO userBO);

    //登陆
    Result<AuthVO> login(String loginName, String password);

    //注销
    Result logout(String loginName);

    //修改账户
    Result update(UserBO userBO);

    //修改密码
    Result updatePassword(UserBO userBO);
}
