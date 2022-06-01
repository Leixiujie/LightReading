package com.cqupt.readingcloud.homepage.common.config;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
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
@MapperScan(basePackages = "com.cqupt.readingcloud.homepage.dao")
public class MybatisConfig {
    public final static String MAPPER_LOCATIONS = "classpath*:mappers/*.xml";

    //工厂配置
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("bookCenterDataSource")DataSource dataSource) throws Exception{
        //设置数据源
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        //添加xml映射
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factory.setMapperLocations(resolver.getResources(MAPPER_LOCATIONS));

        //添加插件
        factory.setPlugins(new Interceptor[]{this.getPageHelper()});
        return factory.getObject();
    }

    @Bean(name = "bookCenterSqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    private Interceptor getPageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();

        //分页尺寸为0，则不分页
        properties.setProperty("pageSizeZero", "true");
        //推断合理页码
        properties.setProperty("reasonable", "true");
        //通过mapper传递分页参数
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        //切换数据源时，自动解析不同数据分页
        properties.setProperty("autoRuntimeDialect", "true");
        pageHelper.setProperties(properties);

        return pageHelper;

    }
}
