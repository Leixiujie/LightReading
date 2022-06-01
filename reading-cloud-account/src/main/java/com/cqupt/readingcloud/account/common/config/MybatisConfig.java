package com.cqupt.readingcloud.account.common.config;

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
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.cqupt.readingcloud.account.dao")
public class MybatisConfig {
    private final static String MAPPER_LOCATIONS = "classpath*:mappers/*.xml";

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("accountCenterDataSource")DataSource dataSource)
            throws Exception{
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

    @Bean(name = "accountCenterSqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //配置分页插件
    private PageHelper getPageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();

        //分页尺寸为0时查询记录不再分页
        properties.setProperty("pageSizeZero", "true");
        //不合理的页码自动换到合适的页码去
        properties.setProperty("reasonable", "true");
        //支持通过Mapper接口参数来传递分页参数
        properties.setProperty("supportMethodsArgument", "true");
        properties.setProperty("params", "count=countSql");
        //根据不同数据源，解析不同数据库的分页
        properties.setProperty("autoRuntimeDialect", "true");
        pageHelper.setProperties(properties);

        return pageHelper;
    }

    /**
     * swagger 配置类
     * http://localhost:8080/swagger-ui.html
     * @author lxj
     * @since 2019-07-04
     */
    @Configuration
    @EnableSwagger2
    public static class AccountSwaggerConfig {

        /**
         * swagger生成
         * @return Docket
         */
        @Bean
        public Docket customDocket() {
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.cqupt.readingcloud.account.controller"))
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
            return docket;
        }

        /**
         * swagger基础信息
         * @return ApiInfo swagger信息
         */
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("账户中心接口")
                    .description("账户中心")
                    .termsOfServiceUrl("")
                    .contact(new Contact("", "", ""))
                    .license("")
                    .licenseUrl("")
                    .version("1.0.0")
                    .build();
        }
    }
}
