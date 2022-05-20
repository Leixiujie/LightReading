package com.cqupt.readingcloud.book.vo;

import lombok.Data;

@Data
public class BookChapterVO {
    private Integer id;
    private String name;
    private String content;

    public BookChapterVO(Integer id, String name, String content){
        this.id = id;
        this.name = name;
        this.content = content;
    }
}
