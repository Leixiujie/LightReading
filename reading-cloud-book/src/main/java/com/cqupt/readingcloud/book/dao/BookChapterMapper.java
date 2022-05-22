package com.cqupt.readingcloud.book.dao;

import com.cqupt.readingcloud.common.pojo.book.BookChapter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookChapterMapper {
    //按ID查
    BookChapter selectById(@Param("id") Integer id);

    //根据书的id来查
    List<BookChapter> findPageWithResult(@Param("bookId") Integer bookId);

    int findPageWithCount(@Param("bookId") Integer bookId);

    @Select("select id from book_chapter where book_id=#{bookId} and sort_number<#{currentSortNum} " +
            "order by sort_number desc limit 1")
    Integer selectPreChapterId(@Param("bookId") Integer bookId, @Param("currentSortNum") Integer currentSortNum);

    @Select("select id from book_chapter where book_id=#{bookId} and sort_number>#{currentSortNum} " +
            "order by sort_number asc limit 1")
    Integer selectNextChapterId(@Param("bookId") Integer bookId, @Param("currentSortNum") Integer currentSortNum);
}
