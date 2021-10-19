package com.key.win.rabbitmq.producer;



import com.key.win.rabbitmq.common.DetailResponse;


public interface MessageProcess<T> {
    DetailResponse process(T message);
}
