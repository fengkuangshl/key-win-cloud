package com.key.win.client.service;

public interface RateLimitService {

	public boolean checkRateLimit(String reqUrl, String accessToken) ;
	
}
