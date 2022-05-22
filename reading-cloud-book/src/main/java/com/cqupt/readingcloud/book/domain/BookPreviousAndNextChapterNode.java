package com.cqupt.readingcloud.book.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookPreviousAndNextChapterNode implements Serializable {
    private static final Long serialVersionUID = 1L;

    // 章节ID
    private Integer id;
    // 章节名称
    private String name;
    // 上一章
    BookPreviousAndNextChapterNode pre;
    // 下一章
    BookPreviousAndNextChapterNode next;

    public BookPreviousAndNextChapterNode(){}

    public BookPreviousAndNextChapterNode(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public BookPreviousAndNextChapterNode(BookPreviousAndNextChapterNode chapterNode){
        this.id = chapterNode.getId();
        this.name = chapterNode.getName();
    }
}
