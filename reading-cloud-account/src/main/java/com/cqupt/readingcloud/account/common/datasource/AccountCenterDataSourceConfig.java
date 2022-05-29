package com.cqupt.readingcloud.account.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class AccountCenterDataSourceConfig {

    //数据源bean
    @Bean(name = "accountCenterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.account-center")
    public DataSource accountCenterDataSource(){
        return new DruidDataSource();
    }

    public DataSourceTransactionManager setTransactionManager(@Qualifier("accountCenterDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
