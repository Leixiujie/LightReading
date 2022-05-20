package com.cqupt.readingcloud.book.vo;

import lombok.Data;

@Data
public class BookVO {
    private Integer id;
    private Integer authorId;
    //分类名
    private Integer dicCategory;
    private String categoryName;
    //频道id: 0:全部；1:男生；2:女生；3出版
    private Integer dicChannel;
    //连载状态
    private Integer dicSerialStatus;
    private String serialStatusName;

    private String bookId;
    //图书名称
    private String bookName;
    //图书评分
    private Integer bookScore;
    //关键词
    private String keyWord;
    //封面
    private String imgUrl;
    //作者名称
    private String authorName;
    //简介
    private String introduction;
    //字数(万)
    private Integer wordCount;
}
