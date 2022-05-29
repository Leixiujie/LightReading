package com.cqupt.readingcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.cqupt.readingcloud.book.feign")
@SpringBootApplication(scanBasePackages = {"com.cqupt.readingcloud.account",
        "com.cqupt.readingcloud.common", "com.cqupt.readingcloud.book.feign"})
public class ReadingCloudAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadingCloudAccountApplication.class, args);
    }

}
