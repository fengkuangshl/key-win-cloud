package com.key.win.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * dto pojo 互转
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */
@Configuration
public class MapperConfig {
	
	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
