package com.key.win.uaa.controller;

import com.google.common.collect.Lists;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.uaa.feign.UserFeignClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@Api(tags = "Aggregate API")
public class AggregateController {
 
	@Autowired
	private UserFeignClient userFeignClient;

	@Autowired
	TaskExecutor taskExecutor;

 
	 
	@PostMapping("/test/aggregate")
	public List<LoginAppUser> hello () throws InterruptedException, ExecutionException{
		List<LoginAppUser> list = Lists.newArrayList();

		CountDownLatch countDownLatch = new CountDownLatch(2);
		

		CompletableFuture<LoginAppUser> userFuture1 = CompletableFuture.supplyAsync(() -> {
			try {

				return userFeignClient.findByMobile("18579068166");

			} catch (Exception e) {
				log.error("落库失败：{}", e.getMessage());
			} finally {
				countDownLatch.countDown();
			}
			return null;

		},taskExecutor);

		CompletableFuture<LoginAppUser> userFuture2 = CompletableFuture.supplyAsync(() -> {
			try {

				return userFeignClient.findByMobile("18579068166");

			} catch (Exception e) {
				log.error("落库失败：{}", e.getMessage());
			} finally {
				countDownLatch.countDown();
			}
			return null;

		},taskExecutor);

		countDownLatch.await();
		list.add(userFuture1.get());
		list.add(userFuture2.get());

		countDownLatch.await();

		return list;
	}

}
