package com.josh.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.josh.repo.user",
        entityManagerFactoryRef = "mysqlEntityManagerFactory",
        transactionManagerRef = "mysqlTransactionManager"
)
@RequiredArgsConstructor
public class MySQLConfig {

    private final Environment environment;

    @Bean(name = "mysqlDataSource")
    @Primary
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.mysql.driver-class-name")));
        dataSource.setUrl(environment.getProperty("spring.datasource.mysql.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.mysql.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.mysql.password"));
        return dataSource;
    }

    @Bean(name = "mysqlEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(mysqlDataSource());
        factoryBean.setPackagesToScan("com.josh.entity.user","com.josh.entity.file");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(adapter);
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        props.put("hibernate.show_sql","true");
        props.put("hibernate.hbm2ddl.auto","update");
        factoryBean.setJpaPropertyMap(props);

        return factoryBean;
    }

    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(mysqlEntityManagerFactory().getObject());
        return jpaTransactionManager;
    }
}
