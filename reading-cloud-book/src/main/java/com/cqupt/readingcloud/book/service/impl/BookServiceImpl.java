package com.cqupt.readingcloud.book.service.impl;

import com.cqupt.readingcloud.book.dao.BookMapper;
import com.cqupt.readingcloud.book.service.BookService;
import com.cqupt.readingcloud.book.vo.BookVO;
import com.cqupt.readingcloud.common.cache.RedisBookKey;
import com.cqupt.readingcloud.common.cache.RedisExpire;
import com.cqupt.readingcloud.common.cache.RedisService;
import com.cqupt.readingcloud.common.constant.CategoryConstant;
import com.cqupt.readingcloud.common.enums.BookSerialStatusEnum;
import com.cqupt.readingcloud.common.pojo.book.Book;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.common.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public Result getBookById(String bookId) {
        String key = RedisBookKey.getBookKey(bookId);
        Book book = this.redisService.getCache(key, Book.class);
        if(book == null){
//            System.out.println(bookId);
            book = this.bookMapper.selectByBookId(bookId);
            //防止缓存穿透，即使没查出来数据，null也缓存
            this.redisService.setExpireCache(key, book, RedisExpire.HOUR);
        }
        return ResultUtil.success(book);
    }

    @Override
    public Result<BookVO> getBookDetails(String bookId) {
        Book book = (Book) this.getBookById(bookId).getData();

        if(book == null) return ResultUtil.notFound().buildMessage("找不到"+bookId+"这本书");

        BookVO vo = new BookVO();

        //分类
        String categoryName = CategoryConstant.categorys.get(book.getDicCategory());
        vo.setCategoryName(categoryName);

        //连载状态
        String serialStatusName = BookSerialStatusEnum.values()[book.getDicSerialStatus()-1].getName();
        vo.setSerialStatusName(serialStatusName);
        return ResultUtil.success(vo);
    }
}
