package com.cqupt.readingcloud.account.controller;

import com.cqupt.readingcloud.common.request.RequestParams;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.account.service.UserLikeSeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "喜欢看")
@RestController
@RequestMapping("account/like-see")
public class LikeSeeController {

    @Autowired
    private UserLikeSeeService userLikeSeeService;

    @ApiOperation(value = "用户喜欢点击接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "value", value = "值:0取消喜欢,1:喜欢", required = true, dataType = "int")
    })
    @PostMapping("/click")
    public Result likeSeeClick(@RequestHeader("userId")Integer userId, @RequestBody RequestParams params){
        String bookId = params.getStringValue("bookId");
        Integer value = params.getIntValue("value");
        return this.userLikeSeeService.likeSeeClick(userId, bookId, value);
    }

    @ApiOperation(value = "获取图书喜欢总数", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", required = true, dataType = "String")
    })
    @GetMapping("/get-count")
    public Result<Integer> getBookLikeCount(String bookId){
        return this.userLikeSeeService.getBookLikeCount(bookId);
    }

    @ApiOperation(value = "获取用户喜欢书单", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页数量", required = true, dataType = "int")
    })
    @GetMapping("/get-books")
    public Result getUserLikeBookList(@RequestHeader("userId") Integer userId, Integer page, Integer limit){
        return this.userLikeSeeService.getUserLikeBookList(userId, page, limit);
    }

    @ApiOperation(value = "用户是否喜欢此书", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", required = true, dataType = "String")
    })
    @GetMapping("/exist-book")
    public Result<Integer> userLikeThisBook(@RequestHeader("userId")Integer userId, String bookId){
        return this.userLikeSeeService.userLikeThisBook(userId, bookId);
    }
}
