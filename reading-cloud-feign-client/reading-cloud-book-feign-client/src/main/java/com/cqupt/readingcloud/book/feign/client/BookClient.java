package com.cqupt.readingcloud.book.feign.client;

import com.cqupt.readingcloud.book.feign.fallback.BookClientFallBack;
import com.cqupt.readingcloud.common.pojo.book.Book;
import com.cqupt.readingcloud.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "book", name = "light-reading-cloud-book", fallbackFactory = BookClientFallBack.class)
public interface BookClient {

    @RequestMapping("/book/getBookById")
    Result<Book> getBookById(@RequestParam("bookId") String bookId);
}
