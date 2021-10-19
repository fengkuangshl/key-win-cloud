package com.key.win.log.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 * @author owen
 * @create 2017年7月2日
 * recordRequestParam:true需要配置log数据源
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

	/**
	 * 模块
	 * @return
	 */
	String module();

	/**
	 * 记录执行参数
	 * @return
	 */
	boolean recordRequestParam() default false;
}
