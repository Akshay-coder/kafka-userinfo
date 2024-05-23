package com.kafka.UserInfoExample.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.kafka.UserInfoExample.constants.KafkaTopics;
import com.kafka.UserInfoExample.dto.DestinationResorts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class DestinationService {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectWriter objectWriter;


    public void produceDestinations(List<DestinationResorts> destinationResortsList) {
        try {
            String destinationResorts = objectWriter.writeValueAsString(destinationResortsList);
            kafkaTemplate.send(KafkaTopics.DESTINATION_RESORTS, destinationResorts);
        } catch (Exception exception) {
//            log.error("Exception reading destinations parse error for {}", destinationResortsList, exception);
        }

    }
}
