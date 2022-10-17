/**
 *
 */
package com.key.win;

import com.key.win.log.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableLogging
@EnableDiscoveryClient
@SpringBootApplication
public class WebSocketCenterApp {

    public static void main(String[] args) {
//		固定端口启动
//		SpringApplication.run(UserCenterApp.class, args);

        //随机端口启动
        SpringApplication app = new SpringApplication(WebSocketCenterApp.class);
        app.run(args);

    }

}
