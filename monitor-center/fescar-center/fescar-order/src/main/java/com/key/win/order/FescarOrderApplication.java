package com.key.win.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.key.win.api.feign")
public class FescarOrderApplication {

    public static void main(String[] args) {
//		固定端口启动
		SpringApplication.run(FescarOrderApplication.class, args);
    }

}
