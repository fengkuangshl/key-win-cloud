package com.key.win.rabbitmq.controller;

import com.key.win.rabbitmq.service.RabbitSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	  @Autowired
	private RabbitSenderService rabbitSenderService ;
	
	@GetMapping("/hello")
	public String hello (){
		
		rabbitSenderService.sendTTLExpireMsg("hello");
		return "hello";
	}
	 
}
