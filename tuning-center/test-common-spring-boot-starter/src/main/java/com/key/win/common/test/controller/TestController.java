package com.key.win.common.test.controller;

import com.key.win.common.annotation.AccessLimit;
import com.key.win.common.test.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

	@Autowired
	 BeanFactory beanFactory;
	 
	 @Autowired
	 LogService logService;
	 
	
	@GetMapping("/test")
	@AccessLimit(seconds=1,maxCount=2,needLogin=false)
	public String opt(){
		
		logService.log();

		return "ok" ;
	}
	
	 
	
	 
}
