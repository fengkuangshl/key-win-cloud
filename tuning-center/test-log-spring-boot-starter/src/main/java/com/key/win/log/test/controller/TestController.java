package com.key.win.log.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Slf4j
@RestController
public class TestController {
	 @Autowired
	    BeanFactory beanFactory;
	 
	
	@GetMapping("/test")
	public String opt(){
		
		log.info("ok");
		CompletableFuture.runAsync(() -> {
			 log.info("oook");

		}, new TraceableExecutorService(beanFactory,  Executors.newFixedThreadPool(10),
		        // 'calculateTax' explicitly names the span - this param is optional
		        "calculateTax"));
		
		return "ok" ;
	}
	
	 
}
