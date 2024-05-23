package com.kafka.UserInfoExample.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.kafka.UserInfoExample.constants.KafkaTopics;
import com.kafka.UserInfoExample.dto.EnrichedUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EnrichUserInfoService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectWriter objectWriter;


    public void produceEnrichUserInfo(List<EnrichedUserInfo> enrichedUserInfos) {
        try {
            String enrichUserInfo = objectWriter.writeValueAsString(enrichedUserInfos);
            log.info("Producing EnrichUserInfo {}", enrichUserInfo);
            kafkaTemplate.send(KafkaTopics.ENRICHED_USER_INFO, enrichUserInfo);
        } catch (Exception exception) {
            log.error("Exception reading enrichedUserInfo's parse error for {}", enrichedUserInfos, exception);
        }

    }
}
