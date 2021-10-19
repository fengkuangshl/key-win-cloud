package com.open.capatcity.message.service;


import com.open.capatcity.message.config.Sink;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerListener {
	@StreamListener(target = Sink.INPUT)
    public void handleGreetings(@Payload Map msg) {
        System.out.println("Received greetings: " +  msg);
    }
}
