package com.sharding.demo.service;

import com.sharding.demo.dao.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 
@Service
public class CompanyServiceImpl implements CompanyService{
	@Autowired
	private CompanyMapper companyMapper;

}