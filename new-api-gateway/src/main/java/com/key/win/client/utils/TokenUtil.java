package com.key.win.client.utils;

import com.key.win.common.constant.UaaConstant;
import com.key.win.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public class TokenUtil {
	public  static String extractToken(ServerHttpRequest request) {
		List<String> strings = request.getHeaders().get(UaaConstant.AUTHORIZATION);
		String authToken = "";
		if(!StringUtil.isEmpty(strings) && strings.get(0).contains("Bearer")){
			authToken = strings.get(0).substring("Bearer".length()).trim();
		}
		if (StringUtils.isBlank(authToken)) {
			strings = request.getQueryParams().get(UaaConstant.TOKEN_PARAM);
			if (!StringUtil.isEmpty(strings)) {
				authToken = strings.get(0);
			}
		}
		return authToken;
	}
}
