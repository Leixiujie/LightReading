package com.cqupt.readingcloud.book.feign.fallback;

import com.cqupt.readingcloud.book.feign.client.BookClient;
import com.cqupt.readingcloud.common.pojo.book.Book;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.common.result.ResultUtil;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookClientFallBack implements FallbackFactory<BookClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookClientFallBack.class);


    @Override
    public BookClient create(Throwable cause) {
        return new BookClient(){

            @Override
            public Result<Book> getBookById(String bookId) {
              LOGGER.error("获取图书：[{}]失败：{}", bookId, cause.getMessage());
              return ResultUtil.success(null);
            };
        };
    }
}
