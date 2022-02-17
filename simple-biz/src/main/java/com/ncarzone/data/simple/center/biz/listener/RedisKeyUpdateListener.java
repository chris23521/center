package com.ncarzone.data.simple.center.biz.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyUpdateListener extends RedisKeyUpdateEventMessageListener {
    public RedisKeyUpdateListener(
        RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("新增或者跟新key:" + message.toString());
    }
}
