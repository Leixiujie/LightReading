package com.cqupt.readingcloud.book.dao;

import com.cqupt.readingcloud.common.pojo.book.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper {

    Book selectById(Integer id);

    Book selectByBookId(String bookId);

    List<Book> findPageWithResult(
            @Param("dicCategory") Integer dicCategory,
            @Param("dicChannel") Integer dicChannel,
            @Param("dicSerialStatus") Integer dicSerialStatus,
            @Param("onlineStatus") Integer onlineStatus,
            @Param("authorId") Integer authorId,
            @Param("bookId") String bookId,
            @Param("bookName") String bookName);

}
