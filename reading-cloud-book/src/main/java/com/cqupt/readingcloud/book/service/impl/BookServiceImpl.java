package com.cqupt.readingcloud.book.service.impl;

import com.cqupt.readingcloud.book.service.BookService;
import com.cqupt.readingcloud.book.vo.BookVO;
import com.cqupt.readingcloud.common.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public Result getBookById(String bookId) {
        return null;
    }

    @Override
    public Result<BookVO> getBookDetails(String bookId) {
        return null;
    }
}
