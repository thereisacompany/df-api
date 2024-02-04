package com.dfrecvcle.dfsystem.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.dfrecvcle.dfsystem.utils.datasource.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.jdbc-url}")
    private String defaultDBUrl;
    @Value("${spring.datasource.username}")
    private String defaultDBUser;
    @Value("${spring.datasource.password}")
    private String defaultDBPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String defaultDBDriverName;

//    @Value("${spring.datasource.datasource2.jdbc-url}")
//    private String sportDBUrl;
//    @Value("${spring.datasource.datasource2.username}")
//    private String sportDBUser;
//    @Value("${spring.datasource.datasource2.password}")
//    private String sportDBPassword;
//    @Value("${spring.datasource.datasource2.driver-class-name}")
//    private String sportDBDriverName;

    @Bean
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        DruidDataSource defaultDataSource = new DruidDataSource();
        defaultDataSource.setUrl(defaultDBUrl);
        defaultDataSource.setUsername(defaultDBUser);
        defaultDataSource.setPassword(defaultDBPassword);
        defaultDataSource.setDriverClassName(defaultDBDriverName);

//        DruidDataSource sportDataSource = new DruidDataSource();
//        sportDataSource.setUrl(sportDBUrl);
//        sportDataSource.setUsername(sportDBUser);
//        sportDataSource.setPassword(sportDBPassword);
//        sportDataSource.setDriverClassName(sportDBDriverName);

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("live", defaultDataSource);
//        targetDataSources.put("sport", sportDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource); // 默认数据源
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);
        factoryBean.setTypeAliasesPackage("com.dfrecvcle.dfsystem.**.entities");
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper_xml/*.xml"));
        factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return factoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
