package com.key.win.common.annotation;

import com.key.win.common.selector.ApiIdempotentImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动幂等拦截器
 * @author gitgeek
 * @create 2019年9月5日
 * 自动装配starter
 * 选择器
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Import(ApiIdempotentImportSelector.class)
public @interface EnableApiIdempotent {
}
