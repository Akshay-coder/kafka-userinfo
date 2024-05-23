package com.kafka.UserInfoExample.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.UserInfoExample.dto.DestinationResorts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

@Slf4j
public class ObjectMapperUtils {

    private static String destinationFileName = "classpath:destination-resort.json";

    public static List<DestinationResorts> readDestinationJsonFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassLoader classLoader = Object.class.getClassLoader();
            ResourceLoader resourceLoader = new DefaultResourceLoader(classLoader);
            Resource resource = resourceLoader.getResource(destinationFileName);
            log.info("Loading destinations-resorts from file: {}", resource.getFilename());

            List<DestinationResorts> destinationsResorts = objectMapper.readValue(resource.getFile(), new TypeReference<List<DestinationResorts>>() {
            });
            log.info("Loaded {} destinations-resorts", destinationsResorts.size());
            return destinationsResorts;

        } catch (Exception e) {
            log.error("Error loading destinations-resorts from file: {}", e.getMessage());
            throw new RuntimeException("Error loading destinations-resorts from file", e);
        }
    }
}
