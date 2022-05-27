package com.cqupt.readingcloudaccount.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
@ConfigurationProperties(prefix = "spring.thread-pool.bookshelf")
@Data
public class BookshelfThreadPoolConfig {

    private int corePoolSize;

    private int maximumPoolSize;

    private Long keepAliveTime;

    private int queueCapacity;

    @Bean(value = "userBookShelfQueueThreadPool")
    public ExecutorService buildUserBookShelfQueueThreadPool(){
        //初始化ThreadFactory的线程名格式
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("user-bookshelf-queue-thread-%d").build();

        ExecutorService pool = new ThreadPoolExecutor(this.getCorePoolSize(),
                this.getMaximumPoolSize(), this.getKeepAliveTime(), TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(this.getQueueCapacity()),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        return pool;
    }

}
