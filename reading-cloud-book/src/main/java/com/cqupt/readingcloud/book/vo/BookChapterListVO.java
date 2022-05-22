package com.cqupt.readingcloud.book.vo;

public class BookChapterListVO {
    private static final Long serialVersionUID = 1L;

    //主键ID
    protected Integer id;

    //所属图书
    private Integer bookId;

    //章节名称
    private String name;

    //锁章状态(0:无,1:锁章)
    private Boolean lockStatus;

    private Integer sortNumber;
}
