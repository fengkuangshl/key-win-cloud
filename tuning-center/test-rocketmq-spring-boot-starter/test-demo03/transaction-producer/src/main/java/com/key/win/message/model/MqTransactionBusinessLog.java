package com.key.win.message.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MqTransactionBusinessLog  {
	private String id ;
	private String transactionId ;
	private String description ;
	 
	
	
}
