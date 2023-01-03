package com.key.win.activiti.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;


public class DefaultRedisActivitiReceiver implements RedisActivitiReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext;

    public DefaultRedisActivitiReceiver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * 此方法会被反射调用
     */
    @Override
    public void receiveActivitiMessage(String message) {
        logger.info("监听到的任务信息："+message);


    }
}