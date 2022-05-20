package com.cqupt.readingcloud.book.dao;

import com.cqupt.readingcloud.common.pojo.book.BookAuthor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookAuthorMapper {
    List<BookAuthor> findPageWithResult(@Param("name") String name);
    Integer findPageWithCount(String name);
}
