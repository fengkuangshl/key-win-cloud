package com.sharding.demo.service;

import com.sharding.demo.dao.ProvinceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 
@Service
public class ProvinceServiceImpl implements ProvinceService{
	@Autowired
	private ProvinceMapper provinceMapper;

}