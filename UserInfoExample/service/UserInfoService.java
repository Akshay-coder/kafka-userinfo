package com.kafka.UserInfoExample.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.kafka.UserInfoExample.constants.KafkaTopics;
import com.kafka.UserInfoExample.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserInfoService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectWriter objectWriter;

    public void produceUserInfoData(List<UserInfo> userInfoList){
        try {
            String userInfoJson = objectWriter.writeValueAsString(userInfoList);
            kafkaTemplate.send(KafkaTopics.USER_INFO, userInfoJson);
        } catch (Exception exception) {
            log.error("Exception reading userinfo parse error for {}", userInfoList, exception);
        }
    }
}
