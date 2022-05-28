package com.cqupt.readingcloudaccount.dao;

import com.cqupt.readingcloud.common.pojo.account.UserBookshelf;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户书架DAO
 */
@Repository
public interface UserBookshelfMapper {

    int deleteById(Integer id);

    int insert(UserBookshelf userBookshelf);

    int updateByUserIdAndBookId(UserBookshelf userBookshelf);

    int selectCountByUserAndBookId(@Param("userId") Integer userId, @Param("bookId") String bookId);

    UserBookshelf selectById(Integer id);

    List<UserBookshelf> findPageWithResult(@Param("userId") Integer userId);

}
