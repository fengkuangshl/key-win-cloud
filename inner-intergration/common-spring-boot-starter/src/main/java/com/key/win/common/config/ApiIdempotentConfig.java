package com.key.win.common.config;

import com.key.win.common.interceptor.AccessLimitInterceptor;
import com.key.win.common.interceptor.ApiIdempotentInterceptor;
import com.key.win.redis.util.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * blog: https://blog.51cto.com/13005375
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class ApiIdempotentConfig implements WebMvcConfigurer {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessLimitInterceptor(redisUtil));
        registry.addInterceptor(new ApiIdempotentInterceptor(redisTemplate)).addPathPatterns("/**");


    }
}
