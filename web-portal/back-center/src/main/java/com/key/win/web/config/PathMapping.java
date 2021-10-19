package com.key.win.web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class PathMapping implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bpmn/**").addResourceLocations("file:D:/dev-env/workspace-key-win-cloud/key-win-cloud/web-portal/back-center/src/main/activiti/static/bpmn/");
    }
}
