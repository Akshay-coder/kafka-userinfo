package com.kafka.UserInfoExample.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.UserInfoExample.dto.DestinationResorts;
import com.kafka.UserInfoExample.dto.EnrichedUserInfo;
import com.kafka.UserInfoExample.dto.UserInfo;
import com.kafka.UserInfoExample.service.DestinationService;
import com.kafka.UserInfoExample.service.EnrichUserInfoService;
import com.kafka.UserInfoExample.utils.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kafka.UserInfoExample.constants.KafkaTopics.*;

@Service
@Slf4j
public class UserInfoConsumer {

    Map<String, DestinationResorts> destinationResortsMap=new HashMap<>();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DestinationService destinationService;

    @Autowired
    EnrichUserInfoService enrichUserInfoService;



    @KafkaListener(topics = DESTINATION_RESORTS, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeDestinationResort(String destinationResort) {
        try {
            List<DestinationResorts> destinationResortsResortList = objectMapper.readValue(destinationResort, new TypeReference<List<DestinationResorts>>() {
            });
            destinationResortsMap = destinationResortsResortList.stream().collect(Collectors.toMap(DestinationResorts::getName, Function.identity(), (destination1, destination2) -> destination2));
        } catch (Exception e) {
            log.error("Error reading destination kafka message");
        }
    }


    @KafkaListener(topics = USER_INFO, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserInfo(String userInfo){
        try {
            List<UserInfo> userInfoList = objectMapper.readValue(userInfo, new TypeReference<List<UserInfo>>(){});
            createEnrichedUserInfo(userInfoList);
        } catch (Exception e) {
            log.error("Error reading destination kafka message");
        }
    }

    private void createEnrichedUserInfo(List<UserInfo> userInfoList) {
        List<EnrichedUserInfo> enrichedUserInfos = new ArrayList<>();
        List<UserInfo> destinationNotPresent = new ArrayList<>();
        userInfoList.stream().forEach(userInfo -> {
            EnrichedUserInfo enrichedUserInfo = new EnrichedUserInfo();
            if (destinationResortsMap.containsKey(userInfo.getDestination())) {
                enrichedUserInfo = modelMapper.map(userInfo, EnrichedUserInfo.class);
                enrichedUserInfo.setDestinationResorts(destinationResortsMap.get(userInfo.getDestination()));
                enrichedUserInfos.add(enrichedUserInfo);
            } else {
                destinationNotPresent.add(userInfo);
            }
        });

        if (destinationNotPresent != null && !destinationNotPresent.isEmpty()) {
            enrichedUserInfos.addAll(executeFallbackMechanism(destinationNotPresent));
        }

    }

    private List<EnrichedUserInfo> executeFallbackMechanism(List<UserInfo> destinationNotPresent) {
        destinationService.produceDestinations(ObjectMapperUtils.readDestinationJsonFile());
        List<EnrichedUserInfo> enrichedUserInfos = new ArrayList<>();
        destinationNotPresent.stream().forEach(userInfo -> {
            EnrichedUserInfo enrichedUserInfo = new EnrichedUserInfo();
            if (destinationResortsMap.containsKey(userInfo.getDestination())) {
                enrichedUserInfo = modelMapper.map(userInfo, EnrichedUserInfo.class);
                enrichedUserInfo.setDestinationResorts(destinationResortsMap.get(userInfo.getDestination()));
                enrichedUserInfos.add(enrichedUserInfo);
            } else {
                log.error("Destination {} not present for {}", userInfo.getDestination(), userInfo.getUser_id());
            }
        });

        return enrichedUserInfos;
    }
}
