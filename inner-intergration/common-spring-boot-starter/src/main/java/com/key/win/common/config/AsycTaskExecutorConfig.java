package com.key.win.common.config;

import com.key.win.common.async.ContextCopyingDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 * 线程池配置、启用异步
 * @Async quartz 需要使用
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */
@EnableAsync(proxyTargetClass = true)
@Configuration
public class AsycTaskExecutorConfig {

	/** Set the ThreadPoolExecutor's core pool size.
     * 线程池维护线程的最小数量.
     * */
    private int corePoolSize = 10;
    /** Set the ThreadPoolExecutor's maximum pool size.
     *线程池维护线程的最大数量
     * */
    private int maxPoolSize = 200;
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue.
     *队列最大长度
     * */
    private int queueCapacity = 10;
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("MyExecutor-");
        // for passing in request scope context
        executor.setTaskDecorator(new ContextCopyingDecorator());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
	}
}
