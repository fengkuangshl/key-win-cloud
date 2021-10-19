package com.key.win.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
public class MybatiesConfig {

	/**
	 * 乐观锁
	 * 
	 * @return
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		log.info("乐观锁插件已加载...");
		return new OptimisticLockerInterceptor();
	}

	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		log.info("分页插件已加载...");
		// 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求 默认false
		// paginationInterceptor.setOverflow(false);
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		// paginationInterceptor.setLimit(500);
		// 开启 count 的 join 优化,只针对部分 left join
		// paginationInterceptor.setCountSqlParser(new
		// JsqlParserCountOptimize(true));
//		/paginationInterceptor.setDialectType("sqlite");
		return paginationInterceptor;
	}
}
