package com.cqupt.readingcloud.account.dao;

import com.cqupt.readingcloud.common.pojo.account.UserLikeSee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户喜欢DAO接口
 */
@Repository
public interface UserLikeSeeMapper {

    int deleteByUserIdAndBookId(@Param("userId") Integer userId, @Param("bookId") String bookId);

    int insert(UserLikeSee userLikeSee);

    int selectCountByUserAndBookId(@Param("userId") Integer userId, @Param("bookId") String bookId);

    List<UserLikeSee> findPageWIthResult(@Param("userId") Integer userId);

    Integer findPageWithCount(@Param("bookId") String bookID);

}
