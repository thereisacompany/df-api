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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

public class SportDataSourceConfig {

    @Autowired
    MybatisProperties mybatisProperties;

    @Bean(name = "SportDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.datasource2")
    public DataSource sportDataSource() { return DataSourceBuilder.create().build();}

    @Bean(name = "SportSessionFactory")
    public SqlSessionFactory sportSessionFactory(@Qualifier("SportDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        bean.setMapperLocations(mybatisProperties.resolveMapperLocations());
        return bean.getObject();
    }

    @Bean(name = "SportTransactionManager")
    public DataSourceTransactionManager sportTransactionManger(@Qualifier("SportDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "SportSessionTemplate")
    public SqlSessionTemplate sportSessionTemplate(@Qualifier("SportSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
