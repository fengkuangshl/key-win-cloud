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
@EnableDiscoveryClient
@SpringBootApplication
@EnableApiIdempotent
public class ParamCenterApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ParamCenterApp.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);
    }
}
