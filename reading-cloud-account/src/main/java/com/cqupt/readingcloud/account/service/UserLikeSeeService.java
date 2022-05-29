package com.cqupt.readingcloud.account.service;

import com.cqupt.readingcloud.common.result.Result;

public interface UserLikeSeeService {

    /**
     * 单击喜欢
     * @param userId
     * @param bookId
     * @param value 0：取消喜欢 1：喜欢
     * @return
     */
    Result likeSeeClick(Integer userId, String bookId, Integer value);

    /**
     * 获取图书喜欢数量
     * @param bookId
     * @return
     */
    Result<Integer> getBookLikeCount(String bookId);

    /**
     * 获取喜欢的书的列表
     * @param userId
     * @param page 页码
     * @param limit 页大小
     * @return
     */
    Result getUserLikeBookList(Integer userId, Integer page, Integer limit);

    /**
     * 用户是否喜欢此书
     * @param userId
     * @param bookId
     * @return
     */
    Result userLikeThisBook(Integer userId, String bookId);

}
