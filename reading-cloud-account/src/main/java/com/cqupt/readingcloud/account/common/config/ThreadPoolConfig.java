package com.cqupt.readingcloud.account.common.config;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.thread-pool.common")
public class ThreadPoolConfig {
    private int corePoolSize;
    private int maximumPoolSize;
    private Long keepAliveTime;
    private int queueCapacity;

    public ExecutorService buildCommonQueueThreadPool(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("common-queue-thread-%d").build();

        //实例化线程池
        ExecutorService pool = new ThreadPoolExecutor(this.getCorePoolSize(),
                this.getMaximumPoolSize(), this.getKeepAliveTime(),
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(this.getQueueCapacity()),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        return pool;
    }


}
