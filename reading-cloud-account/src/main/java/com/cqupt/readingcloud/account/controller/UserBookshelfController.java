package com.cqupt.readingcloud.account.controller;

import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.account.bo.UserBookshelfBO;
import com.cqupt.readingcloud.account.service.UserBookShelfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("account/bookshelf")
public class UserBookshelfController {

    @Autowired
    private UserBookShelfService bookshelfService;

    @ApiOperation(value = "同步书架图书接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int")
    })
    @PostMapping("/sync-book")
    public Result syncUserBookshelf(@RequestHeader("userId") Integer userId, @RequestBody UserBookshelfBO bookshelfBO) {
        return this.bookshelfService.syncUserBookshelf(userId, bookshelfBO);
    }

    @ApiOperation(value = "获取书架图书接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int")
    })
    @GetMapping("/get-books")
    public Result getUserBookshelf(@RequestHeader("userId") Integer userId) {
        return this.bookshelfService.getUserBookShelf(userId);
    }

    @ApiOperation(value = "用户书架是否存在图书", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", required = true, dataType = "String")
    })
    @GetMapping("/exist-book")
    public Result<Integer> userBookshelfExistBook(@RequestHeader("userId") Integer userId, String bookId) {
        return this.bookshelfService.userBookshelfExistBook(userId, bookId);
    }
}
