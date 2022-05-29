package com.cqupt.readingcloud.account.service.task;

import com.cqupt.readingcloud.common.cache.RedisAccountKey;
import com.cqupt.readingcloud.common.cache.RedisService;

public class LikeSeeClickTask implements Runnable{
    private RedisService redisService;
    private String bookId;
    private Integer value;

    @Override
    public void run() {
        if(this.redisService.hashHasKey(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, this.bookId)){
            int val = 1;
            if(value <= 0) val = -1;
            this.redisService.hashIncrement(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, this.bookId, val);
        }
    }

    public LikeSeeClickTask(){}

    public LikeSeeClickTask(RedisService redisService, String bookId, Integer value){
        this.redisService = redisService;
        this.bookId = bookId;
        this.value = value;
    }
}
