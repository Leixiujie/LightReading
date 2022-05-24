package com.cqupt.readingcloud.book.controller;

import com.cqupt.readingcloud.book.service.BookChapterService;
import com.cqupt.readingcloud.book.vo.BookChapterReadVO;
import com.cqupt.readingcloud.common.pojo.book.BookChapter;
import com.cqupt.readingcloud.common.result.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "章节查询接口")
@RequestMapping("book/chapter")
public class BookChapterController {

    @Autowired
    private BookChapterService bookChapterService;

    @ApiOperation(value = "查询图书基本信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name="bookId", value = "图书ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "chapterId", value = "章节ID", dataType = "Integer")})
    @ApiResponses({@ApiResponse(code=200, message = "", response = BookChapter.class)})
    @RequestMapping("/getChapter")
    public Result getChapter(String bookId, Integer chapterId){
        Result result = bookChapterService.getChapterById(bookId, chapterId);
        return bookChapterService.getChapterById(bookId, chapterId);
    }

    @ApiOperation(value = "查询章节列表信息", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name="bookId", value = "图书ID", dataType = "String")})
    @RequestMapping("/getChapterList")
    public Result getBookChapterList(String bookId){
        return this.bookChapterService.getBookChapterListByBookId(bookId);
    }

    @ApiOperation(value = "阅读内容" , httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "chapterId", value = "章节ID", dataType = "Integer")
    })
    @RequestMapping("/readChapter")
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId){
        return this.bookChapterService.readChapter(bookId, chapterId);
    }

}
