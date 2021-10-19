package com.key.win;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.key.win.common.annotation.EnableApiIdempotent;
import com.key.win.log.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApiIdempotent
@EnableLogging
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
public class DynamicDataSourceCenterApp {
    public static void main(String[] args) {
        SpringApplication.run(DynamicDataSourceCenterApp.class, args);
    }
}
