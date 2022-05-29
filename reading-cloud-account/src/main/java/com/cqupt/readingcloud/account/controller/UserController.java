package com.cqupt.readingcloud.account.controller;

import com.cqupt.readingcloud.common.request.RequestParams;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.account.bo.UserBO;
import com.cqupt.readingcloud.account.service.UserService;
import com.cqupt.readingcloud.account.vo.AuthVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "注册用户", httpMethod = "POST")
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Result.class)})
    @PostMapping("/register")
    public Result register(@RequestBody UserBO userBO) {
        return this.userService.register(userBO);
    }

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "loginName", value = "登录名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "登录密码", required = true, dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = AuthVO.class)})
    @PostMapping("/login")
    public Result<AuthVO> login(@RequestBody RequestParams params) {
        String loginName = params.getStringValue("loginName");
        String password = params.getStringValue("password");
        return this.userService.login(loginName, password);
    }
}
