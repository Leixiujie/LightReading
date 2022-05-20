package com.cqupt.readingcloud.book.vo;

import lombok.Data;

@Data
public class BookChapterReadVO {
    private BookChapterVO current;
    private BookChapterVO pre;
    private BookChapterVO next;
}
