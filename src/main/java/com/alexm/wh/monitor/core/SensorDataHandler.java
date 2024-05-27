package com.alexm.wh.monitor.core;

import com.alexm.wh.monitor.domain.SensorData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SensorDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(SensorDataHandler.class);

    //@Autowired
    //private JmsTemplate jmsTemplate;

    private final CentralMonitoringService centralMonitoringService;

    public SensorDataHandler(CentralMonitoringService centralMonitoringService) {
        this.centralMonitoringService = centralMonitoringService;
    }

    public void handleSensorData(String name, String ip, String sensorType, String data) throws JsonProcessingException {
        logger.info("{} Sensor Data: {}", sensorType, data);


        Map<String, String> parsedDataMap = parseKeyValueString(data);

        SensorData sensorData = new SensorData(
                name,
                ip, sensorType,
                parsedDataMap.get("sensor_id"),
                Float.parseFloat(parsedDataMap.get("value")));


        // Convert SensorData to JSON
        String jsonString = SensorDataJsonConverter.toJson(sensorData);

        centralMonitoringService.receiveSensorData(jsonString);
        //jmsTemplate.convertAndSend("sensorDataQueue", sensorType + ":" + data);
    }

    private static Map<String, String> parseKeyValueString(String input) {
        return Arrays.stream(input.split(";"))
                .map(String::trim)
                .map(s -> s.split("="))
                .collect(Collectors.toMap(
                        arr -> arr[0].trim(),
                        arr -> arr[1].trim()
                ));
    }
}
