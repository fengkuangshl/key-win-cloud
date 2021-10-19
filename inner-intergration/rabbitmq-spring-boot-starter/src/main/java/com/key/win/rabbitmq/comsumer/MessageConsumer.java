package com.key.win.rabbitmq.comsumer;


import com.key.win.rabbitmq.common.DetailResponse;

/**
 * Created by littlersmall on 16/5/12.
 */
public interface MessageConsumer {
    DetailResponse consume();
}
