package com.kafka.UserInfoExample.utils;

import com.kafka.UserInfoExample.dto.DestinationResorts;
import com.kafka.UserInfoExample.service.DestinationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@EnableScheduling
public class SchedulerDestinationProducer implements CommandLineRunner {

    @Autowired
    private DestinationService destinationService;

    public static final long INTERVAL =60000;

    @Override
    public void run(String... args) throws Exception {
        List<DestinationResorts> destinationResortsList = ObjectMapperUtils.readDestinationJsonFile();
        log.info("Read destinations from file", destinationResortsList);
        destinationService.produceDestinations(destinationResortsList);

    }

    @Scheduled(fixedDelay = INTERVAL)
    public void produceDestinationByInterval() throws Exception {
        run();
    }
}
