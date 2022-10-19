package com.key.win;


import com.key.win.common.config.TraceFilterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

 
@EnableCircuitBreaker
@EnableDiscoveryClient
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TraceFilterConfig.class))
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApiGateWayApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiGateWayApp.class, args);
    }
}