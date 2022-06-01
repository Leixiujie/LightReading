package com.cqupt.readingcloud.homepage.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BookCenterDataSourceConfig {

    @Bean(name = "bookCenterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.book-center")
    public DataSource bookCenterDataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "bookCenterTransactionManager")
    public DataSourceTransactionManager setTransactionManager(@Qualifier("bookCenterDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}
