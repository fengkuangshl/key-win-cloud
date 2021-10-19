package com.key.win.redis.prefix;

public interface KeyPrefix {
		
	public int expireSeconds();
	
	public String getPrefix();
	
}
