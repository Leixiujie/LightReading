package com.cqupt.readingcloud.book.service;

import com.cqupt.readingcloud.book.vo.BookChapterReadVO;
import com.cqupt.readingcloud.common.pojo.book.BookChapter;
import com.cqupt.readingcloud.common.result.Result;

public interface BookChapterService {

    //获取章节目录
    Result getBookChapterListByBookId(String bookId);

    //获取章节内容
    Result<BookChapter> getChapterById(String bookId, Integer chapterId);

    //阅读章节
    Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId);

}
