package com.key.win.activiti.redis;


public interface RedisActivitiReceiver {
    String RECEIVER_METHOD_NAME = "receiveActivitiMessage";
    String REDIS_RECEIVER_NAME  = "redisActivitiReceiver";
    String CHANNEL = "activiti";
    /**
     * 回调方法
     * @param message 接收到的消息
     */
    void receiveActivitiMessage(String message);
}
