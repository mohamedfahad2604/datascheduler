
package com.customs360.bizz.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.customs360.bizz")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.customs360.bizz.repository.first",
        entityManagerFactoryRef = "firstEntityManagerFactory"
)
public class AppConfig {

    @Primary
    @Bean(name = "firstDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.first.hikari")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second.hikari")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "thirdDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.third.hikari")
    public DataSource thirdDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "fourthDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.fourth.hikari")
    public DataSource fourthDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "firstEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource firstDataSource) {
        return builder
                .dataSource(firstDataSource)
                .packages("com.customs360.bizz.model.first")
                .persistenceUnit("first")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondDataSource") DataSource secondDataSource) {
        return builder
                .dataSource(secondDataSource)
                .packages("com.customs360.bizz.model.second")
                .persistenceUnit("second")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean(name = "thirdEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean thirdEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("thirdDataSource") DataSource thirdDataSource) {
        return builder
                .dataSource(thirdDataSource)
                .packages("com.customs360.bizz.model.third") // Adjust the package to your entity classes
                .persistenceUnit("third")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }
    
    @Bean(name = "fourthEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean fourthEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fourthDataSource") DataSource fourthDataSource) {
        return builder
                .dataSource(fourthDataSource)
                .packages("com.customs360.bizz.model.fourth") // Adjust the package to your entity classes
                .persistenceUnit("fourth")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }
    
    private Map<String, Object> getHibernateProperties(String dialect) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        // Add any other Hibernate properties you may need

        return properties;
    }
    
    
    /*
     
     // to change based on the server
      
          @Primary
    @Bean(name = "tnets41DC1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tnets41DC1.hikari")
    public DataSource tnets41DC1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "tnets41DC2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tnets41DC2.hikari")
    public DataSource tnets41DC2DataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "tnets41HqDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tnets41Hq.hikari")
    public DataSource tnets41HqDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "tnetsShipperDataDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tnetsShipperData.hikari")
    public DataSource tnetsShipperDataDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "firstEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tnets41DC1DataSource") DataSource tnets41DC1DataSource) {
        return builder
                .dataSource(tnets41DC1DataSource)
                .packages("com.customs360.bizz.model.first")
                .persistenceUnit("first")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tnets41DC2DataSource") DataSource tnets41DC2DataSource) {
        return builder
                .dataSource(tnets41DC2DataSource)
                .packages("com.customs360.bizz.model.second")
                .persistenceUnit("second")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean(name = "thirdEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean thirdEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tnets41HqDataSource") DataSource tnets41HqDataSource) {
        return builder
                .dataSource(tnets41HqDataSource)
                .packages("com.customs360.bizz.model.third")
                .persistenceUnit("third")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }
    
    @Bean(name = "fourthEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean fourthEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tnetsShipperDataDataSource") DataSource tnetsShipperDataDataSource) {
        return builder
                .dataSource(tnetsShipperDataDataSource)
                .packages("com.customs360.bizz.model.fourth")
                .persistenceUnit("fourth")
                .properties(getHibernateProperties("org.hibernate.dialect.MySQLDialect"))
                .build();
    }
    
    private Map<String, Object> getHibernateProperties(String dialect) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        // Add any other Hibernate properties you may need

        return properties;
    }
     
      */

}
