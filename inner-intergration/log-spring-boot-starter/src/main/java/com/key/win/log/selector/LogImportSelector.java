package com.key.win.log.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author owen
 * log-spring-boot-starter 自动装配 
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */


public class LogImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		
		return new String[] { 
				"com.key.win.log.aop.LogAnnotationAOP",
//				"com.key.win.log.config.SentryAutoConfig",
				"com.key.win.log.service.impl.LogServiceImpl",
				"com.key.win.log.config.LogAutoConfig"
				
		};
	}

}