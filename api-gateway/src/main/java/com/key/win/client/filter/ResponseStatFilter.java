package com.key.win.client.filter;

import com.key.win.common.constant.TraceConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * 请求完成后，将trace_id设置到response header里面进行传递
 * @author gitgeek
 *
 */
@Slf4j
@Component
public class ResponseStatFilter extends ZuulFilter {
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	private static final String FILTER_TYPE = "post";

	@Override
	public String filterType() {
		return FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		return SHOULD_FILTER;
	}

	@Override public Object run() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		String url = requestContext.getRequest().getRequestURL().toString();
		String traceId =  MDC.get(TraceConstant.LOG_B3_TRACEID) ;
		log.info("response url " + url + ", traceId = " + traceId);
		requestContext.getResponse().addHeader(TraceConstant.HTTP_HEADER_TRACE_ID, traceId); 
		return null;
	}
	}
