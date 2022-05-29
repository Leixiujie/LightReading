package com.cqupt.readingcloud.account.service;

import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.account.bo.UserBookshelfBO;

public interface UserBookShelfService {
    /**
     * 同步用户书架
     * @param userId
     * @param bookshelf
     * @return
     */
    Result syncUserBookshelf(Integer userId, UserBookshelfBO bookshelf);

    /**
     * 获取用户书架
     * @param userId
     * @return
     */
    Result getUserBookShelf(Integer userId);

    /**
     * 用户书架是否存在该图书
     * @param userId
     * @param bookId
     * @return
     */
    Result userBookshelfExistBook(Integer userId, String bookId);
}
