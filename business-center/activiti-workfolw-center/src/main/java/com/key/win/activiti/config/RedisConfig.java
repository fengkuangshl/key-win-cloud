package com.key.win.activiti.config;

import com.key.win.activiti.redis.DefaultRedisActivitiReceiver;
import com.key.win.activiti.redis.RedisActivitiReceiver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Bean(RedisActivitiReceiver.REDIS_RECEIVER_NAME)
    public RedisActivitiReceiver redisReceiver() {
        return new DefaultRedisActivitiReceiver(getApplicationContext());
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(@Qualifier(RedisActivitiReceiver.REDIS_RECEIVER_NAME) RedisActivitiReceiver redisReceiver) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisReceiver, RedisActivitiReceiver.RECEIVER_METHOD_NAME);
        RedisSerializer redisObjectSerializer = new GenericJackson2JsonRedisSerializer();
        messageListenerAdapter.setSerializer(redisObjectSerializer);
        return messageListenerAdapter;
    }

    @Bean("redisMessageListenerContainer")
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(RedisActivitiReceiver.CHANNEL));
        return container;
    }
}

