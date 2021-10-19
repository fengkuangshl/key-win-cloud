package com.key.win;

import com.key.win.common.annotation.EnableApiIdempotent;
import com.key.win.common.port.PortApplicationEnvironmentPreparedEventListener;
import com.key.win.log.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableLogging
@EnableApiIdempotent
@SpringBootApplication
@EnableDiscoveryClient
public class JpaRpcTemplateApplication {
    public static void main(String[] args) {
//		固定端口启动
//		SpringApplication.run(UserCenterApp.class, args);

        //随机端口启动
        SpringApplication app = new SpringApplication(JpaRpcTemplateApplication.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);

    }
}
