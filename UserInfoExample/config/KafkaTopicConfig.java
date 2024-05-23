package com.kafka.UserInfoExample.config;

import com.kafka.UserInfoExample.constants.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Arrays;
import java.util.Collection;

@Configuration
@Slf4j
public class KafkaTopicConfig {

    @Bean
    public Collection<NewTopic> createTopics() {
        log.info("Creating topics of kafka");
        return Arrays.asList(
                TopicBuilder.name(KafkaTopics.USER_INFO).build(),
                TopicBuilder.name(KafkaTopics.DESTINATION_RESORTS).build(),
                TopicBuilder.name(KafkaTopics.ENRICHED_USER_INFO).build());
    }
}
