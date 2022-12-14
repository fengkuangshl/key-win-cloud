package com.key.win.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
* 非网关应用限流
* blog: https://blog.51cto.com/13005375 
* code: https://gitee.com/owenwangwen/key-win-cloud
*/
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
	int seconds();
	int maxCount();
	boolean needLogin() default true;
}
