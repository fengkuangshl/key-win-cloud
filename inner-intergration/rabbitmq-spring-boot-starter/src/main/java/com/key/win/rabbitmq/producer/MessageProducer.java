package com.key.win.rabbitmq.producer;

import com.key.win.rabbitmq.common.DetailResponse;

public interface MessageProducer {


    DetailResponse send(Object message);

}