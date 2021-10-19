package com.key.win.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
public class CacheConfig {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private SimpleCacheErrorHandler simpleCacheErrorHandler;

	@Autowired
	private CacheOperationSource cacheOperationSource;

	@Autowired
	private CacheResolver cacheResolver;

	@Bean
	public CacheOperationSource createCacheOperationSource(){
		return new AnnotationCacheOperationSource();
	}

	@Bean
	public SimpleCacheErrorHandler createSimpleCacheErrorHandler() {
		return new SimpleCacheErrorHandler();
	}

	@Bean
	public CacheResolver createCacheResolver() {
		return new SimpleCacheResolver(cacheManager);
	}

	/**
	 * 自定义缓存拦截器 其主要目地：防止缓存击穿 当请求过来，缓存中没有数据，执行目标方法时， 在这块加锁，让之后其它请求都在锁外边等着，
	 * 待当前请求完成并把完回数据添加到缓存中，其它请求就可以缓存中获取数据
	 * 
	 * @return
	 */
	@Bean(name = "mycacheAdvice")
	public MyCacheAspectSupport createCacheAspectSupport() {
		MyCacheAspectSupport mcas = new MyCacheAspectSupport();
		mcas.setCacheManager(cacheManager);
		mcas.setErrorHandler(simpleCacheErrorHandler);
		mcas.setBeanFactory(beanFactory);
		mcas.setCacheResolver(cacheResolver);
		mcas.setCacheOperationSources(cacheOperationSource);
		return mcas;
	}

	/**
	 * BeanFactoryCacheOperationSourceAdvisor 继承了 AbstractBeanFactoryPointcutAdvisor
	 * 在spring 中的效果就是，在每个bean的初始化时 (每个bean都会被加载成 advised 对象 -> 有 targetSource 和
	 * Advisor[] 数组)
	 * 每个bean被调用方法的时候都是先遍历advisor的方法，然后在调用原生bean(也就是targetSource)的方法，实现了aop的效果
	 * 
	 * bean 加载的时候 BeanFactoryCacheOperationSourceAdvisor 的 getPointcut()-> 也就是
	 * CacheOperationSourcePointcut 就会被获取，然后调用
	 * CacheOperationSourcePointcut.matches()方法, 用来匹配对应的bean 假设bean 在
	 * BeanFactoryCacheOperationSourceAdvisor 的扫描中 matchs() 方法返回了true 结果就是
	 * 在每个bean的方法被调用的时候 CacheInterceptor 中的 invoke() 方法就会被调用
	 * 
	 * @return
	 */
	@Bean
	public BeanFactoryCacheOperationSourceAdvisor createBeanFactoryCacheOperationSourceAdvisor() {
		BeanFactoryCacheOperationSourceAdvisor bfcosa = new BeanFactoryCacheOperationSourceAdvisor();
		bfcosa.setAdviceBeanName("mycacheAdvice");
		bfcosa.setCacheOperationSource(cacheOperationSource);
		return bfcosa;
	}
}
