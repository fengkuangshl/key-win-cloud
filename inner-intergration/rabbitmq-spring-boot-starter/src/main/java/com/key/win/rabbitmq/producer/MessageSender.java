package com.key.win.rabbitmq.producer;

import com.key.win.rabbitmq.common.DetailResponse;

public interface MessageSender {


    DetailResponse send(Object message);

    DetailResponse send(MessageWithTime messageWithTime);
}