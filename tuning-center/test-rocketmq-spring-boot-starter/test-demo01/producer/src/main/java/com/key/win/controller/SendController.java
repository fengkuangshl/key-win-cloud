package com.key.win.controller;

import com.key.win.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

	@Autowired
	private SendService sendService ;
	
	@GetMapping("/send")
	public String send (){
		sendService.send();
		return "success ";
	}
}
