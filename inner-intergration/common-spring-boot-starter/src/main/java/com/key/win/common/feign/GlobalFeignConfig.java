package com.key.win.common.feign;

import feign.Logger.Level;
import org.springframework.context.annotation.Bean;

/**
 * blog: https://blog.51cto.com/13005375
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */
public class GlobalFeignConfig {

    @Bean
    public Level level() {
        return Level.FULL;
    }
}
