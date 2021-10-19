package com.key.win.redis.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * date: 2021/6/28 16:43 <br>
 *
 * @author: jiashihao <br>
 * version: 1.0 <br>
 * description: RedisPropertiesPlus <br>
 *     重写：redis 获取配置恩文件类
 */
@RefreshScope
@Component
public class RedisPropertiesPlus extends RedisProperties {
}
