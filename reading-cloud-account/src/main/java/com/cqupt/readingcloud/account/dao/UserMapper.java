package com.cqupt.readingcloud.account.dao;

import com.cqupt.readingcloud.common.pojo.account.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int insert(User user);

    int updateByLoginName(User user);

    User selectByLoginName(String loginName);
}
