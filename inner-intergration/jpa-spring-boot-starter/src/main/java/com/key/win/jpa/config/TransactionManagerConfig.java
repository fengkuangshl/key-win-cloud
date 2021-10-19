package com.key.win.jpa.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class TransactionManagerConfig implements TransactionManagementConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionManagerConfig.class);


    @Resource
    private JpaProperties jpaProperties;

    @Resource
    private HibernateProperties hibernateProperties;

    @Resource
    private DataSource dataSourceCore;

    @Resource(name="txManager2")
    private PlatformTransactionManager txManager2;


    private Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }


    @Bean(name = "txAdvice")
    public TransactionInterceptor getAdvisor() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("find*", "PROPAGATION_REQUIRED,-Exception,readOnly");
        properties.setProperty("get*", "PROPAGATION_REQUIRED,-Exception,readOnly");
        properties.setProperty("query*", "PROPAGATION_REQUIRED,-Exception,readOnly");
        properties.setProperty("*", "PROPAGATION_REQUIRED,-Exception");
        TransactionInterceptor tsi = new TransactionInterceptor(txManager2(), properties);
        return tsi;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaPropertyMap(getVendorProperties());
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan("com.key.win.**.model");
        factory.setDataSource(dataSourceCore);
        return factory;
    }

    @Bean
    public BeanNameAutoProxyCreator txProxy() {
        BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
        creator.setInterceptorNames("txAdvice");
        creator.setBeanNames("*Impl", "*Reository");
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

    // 创建事务管理器2
    @Bean(name = "txManager2")
    public PlatformTransactionManager txManager2() {
        return new JpaTransactionManager(entityManagerFactory().getObject());

    }

    // 实现接口 TransactionManagementConfigurer 方法，其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager2;
    }
}
