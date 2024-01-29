package com.dfrecvcle.dfsystem.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

public class LiveDataSourceConfig {

    @Autowired
    MybatisProperties mybatisProperties;

    @Bean(name = "LiveDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.datasource1")
    public DataSource liveDataSource() { return DataSourceBuilder.create().build();}

    @Bean(name = "LiveSessionFactory")
    @Primary
    public SqlSessionFactory liveSessionFactory(@Qualifier("LiveDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        bean.setMapperLocations(mybatisProperties.resolveMapperLocations());
        return bean.getObject();
    }

    @Bean(name = "LiveTransactionManager")
    @Primary
    public DataSourceTransactionManager liveTransactionManger(@Qualifier("LiveDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "LiveSessionTemplate")
    @Primary
    public SqlSessionTemplate liveSessionTemplate(@Qualifier("LiveSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
