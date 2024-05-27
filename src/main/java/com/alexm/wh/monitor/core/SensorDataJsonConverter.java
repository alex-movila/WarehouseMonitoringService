package com.alexm.wh.monitor.core;

import com.alexm.wh.monitor.domain.SensorData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SensorDataJsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static String toJson(SensorData sensorData) throws JsonProcessingException {
        return objectMapper.writeValueAsString(sensorData);
    }

    public static SensorData fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, SensorData.class);
    }
}