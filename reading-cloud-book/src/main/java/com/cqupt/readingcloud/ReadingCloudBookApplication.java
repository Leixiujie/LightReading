package com.cqupt.readingcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.cqupt.readingcloud.book.dao")
@SpringBootApplication(scanBasePackages = {"com.cqupt.readingcloud.book","com.cqupt.readingcloud.common"})
public class ReadingCloudBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadingCloudBookApplication.class, args);
    }

}
