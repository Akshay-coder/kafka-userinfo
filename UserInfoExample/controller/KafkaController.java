package com.kafka.UserInfoExample.controller;

import com.kafka.UserInfoExample.config.KafkaConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @PostMapping("/create-consumer-group")
    public String createConsumerGroup(@RequestParam String topic, @RequestParam String groupId) {
        ConcurrentMessageListenerContainer<String, String> container = kafkaConsumerConfig.createContainer(topic, groupId);
        container.start();
        return "Consumer group '" + groupId + "' for topic '" + topic + "' created and started.";
    }
}
