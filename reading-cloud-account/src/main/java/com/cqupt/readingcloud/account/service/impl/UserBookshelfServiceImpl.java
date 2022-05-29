package com.cqupt.readingcloud.account.service.impl;

import com.cqupt.readingcloud.book.feign.client.BookClient;
import com.cqupt.readingcloud.common.pojo.account.UserBookshelf;
import com.cqupt.readingcloud.common.pojo.book.Book;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.common.result.ResultUtil;
import com.cqupt.readingcloud.account.bo.UserBookshelfBO;
import com.cqupt.readingcloud.account.dao.UserBookshelfMapper;
import com.cqupt.readingcloud.account.service.UserBookShelfService;
import com.cqupt.readingcloud.account.service.task.UserBookshelfTask;
import com.cqupt.readingcloud.account.vo.UserBookshelfVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class UserBookshelfServiceImpl implements UserBookShelfService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserBookshelfMapper bookshelfMapper;

    //线程池
    @Autowired
    private ExecutorService userBookshelfQueueThreadPool;

    //feign接口
    @Autowired
    private BookClient bookClient;

    /**
     * 同步书架
     * @param userId
     * @param bookshelfBO
     * @return
     * 将用户点击新增的书架添加到服务器
     */
    @Override
    public Result syncUserBookshelf(Integer userId, UserBookshelfBO bookshelfBO) {
        UserBookshelf bookshelf = new UserBookshelf();
        BeanUtils.copyProperties(bookshelfBO, bookshelf);
        bookshelf.setLastReadTime(System.currentTimeMillis());

        //异步处理同步任务
        UserBookshelfTask task = new UserBookshelfTask(bookshelfBO.getSyncType(),
                bookshelf, this.bookshelfMapper, userId);
        this.userBookshelfQueueThreadPool.execute(task);
        return ResultUtil.success();
    }


    /**
     * 获取书架
     * @param userId
     * @return
     * 通过feign去远程调用book服务
     */
    @Override
    public Result getUserBookShelf(Integer userId) {
        List<UserBookshelf> pageWithResult = this.bookshelfMapper.findPageWithResult(userId);
        List<UserBookshelfVO> bookshelfs = new ArrayList<>();
        for(int i=0; i<pageWithResult.size(); i++){
            UserBookshelf bookshelf = pageWithResult.get(i);
            Book book = this.bookClient.getBookById(bookshelf.getBookId()).getData();
            if(book != null){
                UserBookshelfVO vo = new UserBookshelfVO();
                BeanUtils.copyProperties(bookshelf, vo);
                vo.setBookName(book.getBookName());
                vo.setAuthorName(book.getAuthorName());
                vo.setImgUrl(book.getImgUrl());
                bookshelfs.add(vo);
            }
        }
        return ResultUtil.success(bookshelfs);
    }

    /**
     * 查看书是否在书架里
     * @param userId
     * @param bookId
     * @return
     */
    @Override
    public Result userBookshelfExistBook(Integer userId, String bookId) {
        int result = 0;
        try{
            result = this.bookshelfMapper.selectCountByUserAndBookId(userId, bookId);
        }
        catch (Exception e){
            LOGGER.error("查询图书{}是否在书架里异常:{}", userId, e);
        }
        return ResultUtil.success(result);
    }
}
