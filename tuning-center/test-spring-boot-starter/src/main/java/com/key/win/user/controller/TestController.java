package com.key.win.user.controller;

import com.key.win.datasource.constant.DataSourceKey;
import com.key.win.datasource.util.DataSourceHolder;
import com.key.win.user.dao.SysLogDao;
import com.key.win.user.dao.SysUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
	
	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysLogDao sysLogDao;
	
	@GetMapping("/user")
	public List user(){
		return  sysUserDao.findList(null);
	}
	
	
	
	@GetMapping("/log")
	public List log(){
		DataSourceHolder.setDataSourceKey(DataSourceKey.log);
		return  sysLogDao.findList(null);
	}
}
