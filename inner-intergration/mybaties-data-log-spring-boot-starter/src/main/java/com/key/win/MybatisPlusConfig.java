package com.key.win;

import com.key.win.datalog.handle.BaseDataLog;
import com.key.win.datalog.handle.DataUpdateInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * Mybatis-Plus配置
 * </p>
 *
 * @author Tophua
 * @since 2020/5/7
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {
    /**
     * <p>
     * 数据更新操作处理
     * </p>
     *
     * @return com.lith.datalog.handle.DataUpdateInterceptor
     * @author Tophua
     * @since 2020/5/11
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(BaseDataLog.class)
    public DataUpdateInterceptor dataUpdateInterceptor() {
        return new DataUpdateInterceptor();
    }
}
