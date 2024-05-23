package com.kafka.UserInfoExample.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

public class CustomMessageListener implements MessageListener<String, String> {
    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
    }
}
