package com.ncarzone.data.simple.center.biz.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyDeleteListener extends RedisKeyDelEventMessageListener {

    public RedisKeyDeleteListener(
        RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("删除key：" + message.toString());
    }
}
