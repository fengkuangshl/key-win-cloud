package com.key.win.common.config;

/**
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 * 
 */

import com.key.win.common.filter.TraceContextFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@SuppressWarnings("all") 
@ConditionalOnClass(WebMvcConfigurer.class)
public class TraceFilterConfig {
	
	@Bean
    public FilterRegistrationBean requestContextRepositoryFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new TraceContextFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
