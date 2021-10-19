package com.sharding.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

 
@Data
@Builder
@AllArgsConstructor
public class Company {
	/***/
	private String companyId;

	/***/
	private String companyName;

	/***/
	private String address;

	/***/
	private Date createTime;

}