package com.key.win;

import com.key.win.common.annotation.EnableApiIdempotent;
import com.key.win.common.feign.GlobalFeignConfig;
import com.key.win.common.port.PortApplicationEnvironmentPreparedEventListener;
import com.key.win.log.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableLogging
@EnableDiscoveryClient
@SpringBootApplication
@EnableApiIdempotent
@EnableFeignClients(defaultConfiguration= GlobalFeignConfig.class)
public class FeignTemplateClientApplication {
    public static void main(String[] args) {
//		固定端口启动
//		SpringApplication.run(UserCenterApp.class, args);

        //随机端口启动
        SpringApplication app = new SpringApplication(FeignTemplateClientApplication.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);

    }
}
