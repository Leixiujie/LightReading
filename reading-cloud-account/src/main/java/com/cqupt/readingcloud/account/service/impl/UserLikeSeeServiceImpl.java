package com.cqupt.readingcloud.account.service.impl;

import com.cqupt.readingcloud.book.feign.client.BookClient;
import com.cqupt.readingcloud.common.cache.RedisAccountKey;
import com.cqupt.readingcloud.common.cache.RedisExpire;
import com.cqupt.readingcloud.common.cache.RedisService;
import com.cqupt.readingcloud.common.pojo.account.UserLikeSee;
import com.cqupt.readingcloud.common.pojo.book.Book;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.common.result.ResultUtil;
import com.cqupt.readingcloud.common.vo.SimpleBookVO;
import com.cqupt.readingcloud.account.dao.UserLikeSeeMapper;
import com.cqupt.readingcloud.account.service.UserLikeSeeService;
import com.cqupt.readingcloud.account.service.task.LikeSeeClickTask;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class UserLikeSeeServiceImpl implements UserLikeSeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikeSeeServiceImpl.class);

    @Autowired
    private UserLikeSeeMapper likeSeeMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookClient bookClient;

    @Autowired
    private ExecutorService commonQueueThreadPool;

    /**
     *
     * @param userId
     * @param bookId
     * @param value 0：取消喜欢 1：喜欢
     * @return
     * 插入喜欢表，增加喜欢数
     */
    @Override
    public Result likeSeeClick(Integer userId, String bookId, Integer value) {
        try{
            if(value == 0){
                this.likeSeeMapper.deleteByUserIdAndBookId(userId, bookId);
            }
            else{
                UserLikeSee like = new UserLikeSee();
                like.setUserId(userId);
                like.setBookId(bookId);
                this.likeSeeMapper.insert(like);
            }
            LikeSeeClickTask task = new LikeSeeClickTask(redisService, bookId, value);
            this.commonQueueThreadPool.execute(task);
        }
        catch (Exception e){
            LOGGER.error("喜欢操作点击异常:{}",e);
            return ResultUtil.fail();
        }
        return ResultUtil.success();
    }

    /**
     * 喜欢数查询
     * @param bookId
     * @return
     */
    @Override
    public Result<Integer> getBookLikeCount(String bookId) {
        Integer likeCount = this.redisService.getHashVal(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, bookId, Integer.class);
        if(likeCount == null){
            likeCount = this.likeSeeMapper.findPageWithCount(bookId);
            this.redisService.setHashValExpire(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, bookId, likeCount, RedisExpire.HOUR);
        }
        return ResultUtil.success(likeCount);
    }

    /**
     * 查询用户喜欢书单
     * @param userId
     * @param page 页码
     * @param limit 页大小
     * @return
     * 通过远程调用去获取书单，并通过bookId把书的详情信息查询出来。
     */
    @Override
    public Result getUserLikeBookList(Integer userId, Integer page, Integer limit) {
        try{
            PageHelper.startPage(page, limit);
            Page<UserLikeSee> pageWithResult = (Page<UserLikeSee>) this.likeSeeMapper.findPageWithResult(userId);
            List<SimpleBookVO> books = new ArrayList<>();
            for(UserLikeSee likeSee: pageWithResult){
                SimpleBookVO vo = new SimpleBookVO();
                Book book = this.bookClient.getBookById(likeSee.getBookId()).getData();
                if(book != null){
                    BeanUtils.copyProperties(book, vo);
                    books.add(vo);
                }
            }
            return ResultUtil.success(books);
        }
        catch (Exception e){
            LOGGER.error("获取用户[{}]喜欢书单异常:{}", userId, e.toString());
            return ResultUtil.fail();
        }
    }

    @Override
    public Result userLikeThisBook(Integer userId, String bookId) {
        int result = 0;
        try{
            result = this.likeSeeMapper.selectCountByUserAndBookId(userId, bookId);
        }
        catch (Exception e){
            LOGGER.error("查询喜欢书数异常:{}", e.toString());
        }
        return ResultUtil.success(result);
    }
}
