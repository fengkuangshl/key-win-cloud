package com.key.win;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.key.win.common.annotation.EnableApiIdempotent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApiIdempotent
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
public class DataSourceTemplateCenterApp {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceTemplateCenterApp.class, args);
    }
}
