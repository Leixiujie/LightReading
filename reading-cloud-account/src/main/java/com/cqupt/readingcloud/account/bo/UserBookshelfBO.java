package com.cqupt.readingcloud.account.bo;

import lombok.Data;

@Data
public class UserBookshelfBO {
    private Integer id;

    //同步状态，1：新增，2：更新，3：删除
    private int syncType;

    //图书ID
    private String BookId;

    //最后读到章节Id
    private Integer lastChapterId;
}
