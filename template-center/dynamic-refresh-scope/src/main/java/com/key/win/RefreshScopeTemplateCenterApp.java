package com.key.win;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
@SpringBootApplication
public class RefreshScopeTemplateCenterApp {
    public static void main(String[] args) {
//		固定端口启动
        SpringApplication.run(RefreshScopeTemplateCenterApp.class, args);


    }
}
