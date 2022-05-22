package com.cqupt.readingcloud.book.dao;

import com.cqupt.readingcloud.common.pojo.book.BookAuthor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorMapper {
    List<BookAuthor> findPageWithResult(@Param("name") String name);
    Integer findPageWithCount(String name);
}
