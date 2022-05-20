package com.cqupt.readingcloud.book.service;

import com.cqupt.readingcloud.book.vo.BookVO;
import com.cqupt.readingcloud.common.result.Result;

public interface BookService {

    //根据Id查询图书信息
    Result getBookById(String bookId);

    //获取图书详情
    Result<BookVO> getBookDetails(String bookId);

}
