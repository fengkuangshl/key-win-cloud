package com.key.win.client.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class SentinelExceptionHandler extends SentinelGatewayBlockExceptionHandler {

    private List<ViewResolver> viewResolvers;
    private List<HttpMessageWriter<?>> messageWriters;

    public SentinelExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        super(viewResolvers, serverCodecConfigurer);
        this.viewResolvers = viewResolvers;
        this.messageWriters = serverCodecConfigurer.getWriters();
    }

    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable e) {
        String msg = "未知异常";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof FlowException) {
             status = HttpStatus.TOO_MANY_REQUESTS; //429;
            msg = "请求被限流了";
        } else if (e instanceof ParamFlowException) {
            status = HttpStatus.TOO_MANY_REQUESTS; //429;
            msg = "请求被热点参数限流";
        } else if (e instanceof DegradeException) {
            msg = "请求被降级了";
        } else if (e instanceof AuthorityException) {
            msg = "没有权限访问";
            status = HttpStatus.UNAUTHORIZED;
        }else if(e instanceof ResponseStatusException){
            msg = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
        serverHttpResponse.setStatusCode(status);
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        JSONObject message = new JSONObject();
        message.put("code", status.value());
        message.put("msg", msg);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(message.toJSONString().getBytes(Charset.defaultCharset()));
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }
}

