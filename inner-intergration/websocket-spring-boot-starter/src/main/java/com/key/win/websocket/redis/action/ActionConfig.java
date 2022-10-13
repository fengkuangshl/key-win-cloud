package com.key.win.websocket.redis.action;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({SendMessageAction.class, SendMessageForTokenAction.class, BroadcastMessageAction.class, RemoveAction.class})
public class ActionConfig {
}