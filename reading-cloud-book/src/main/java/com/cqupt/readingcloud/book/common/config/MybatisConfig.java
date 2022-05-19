package com.cqupt.readingcloud.book.common.config;


import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.cqupt.readingcloud.book.dao",
            sqlSessionTemplateRef = "bookCenterSqlSessionTemplate")
public class MybatisConfig {
    private final static String MAPPER_LOCATIONS = "classpath*:mappers/*.xml";

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("bookCenterDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factory.setMapperLocations(resolver.getResources(MAPPER_LOCATIONS));

        factory.setPlugins(new Interceptor[]{this.getPageHelper()});
        return factory.getObject();
    }

    private PageHelper getPageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();

        //分页尺寸小于1不分页
        properties.setProperty("pageSizeZero", "true");
        //页码不在合理范围内则差第一页或最后一页
        properties.setProperty("reasonable", "true");
        //支持通过Mapper来传输分页参数
        properties.setProperty("supportMethodsArguments","true");
        properties.setProperty("params", "count=countSql");
        //切换数据源，自动解析不同数据库的分页
        properties.setProperty("autoRuntimeDialect", "true");
        pageHelper.setProperties(properties);

        return pageHelper;
    }

    @Bean(name="bookCenterSqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
