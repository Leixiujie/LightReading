package com.cqupt.readingcloud.account.vo;

import lombok.Data;

@Data
public class UserBookshelfVO {
    private Integer id;

    private Integer userId;

    private String bookId;

    private String bookName;

    private String authorName;

    private String imgUrl;

    //上次阅读章节Id
    private Integer lastChapterId;

    //上次阅读时间
    private Long lastReadTime;
}
