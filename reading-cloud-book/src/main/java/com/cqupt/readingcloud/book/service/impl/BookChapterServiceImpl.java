package com.cqupt.readingcloud.book.service.impl;

import com.cqupt.readingcloud.book.dao.BookChapterMapper;
import com.cqupt.readingcloud.book.service.BookChapterService;
import com.cqupt.readingcloud.book.service.BookService;
import com.cqupt.readingcloud.book.vo.BookChapterReadVO;
import com.cqupt.readingcloud.common.cache.RedisService;
import com.cqupt.readingcloud.common.pojo.book.BookChapter;
import com.cqupt.readingcloud.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookChapterServiceImpl implements BookChapterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookChapterServiceImpl.class);

    @Autowired
    private BookChapterMapper bookChapterMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisService redisService;


    @Override
    public Result getBookChapterListByBookId(String bookId) {
        return null;
    }

    @Override
    public Result<BookChapter> getChapterById(String bookId, Integer chapterId) {
        return null;
    }

    @Override
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId) {
        return null;
    }
}
