package com.alexm.wh.monitor.core;


import com.alexm.wh.monitor.config.AlarmThresholdProperties;
import com.alexm.wh.monitor.domain.SensorData;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.List;


import static com.alexm.wh.monitor.core.SensorDataJsonConverter.fromJson;

@Service
public class CentralMonitoringService {
    private static final Logger logger = LoggerFactory.getLogger(CentralMonitoringService.class);

    private final AlarmThresholdProperties alarmThresholdProperties;

    private List<AlarmThresholdProperties.AlarmThreshold> thresholds;

    public CentralMonitoringService(AlarmThresholdProperties alarmThresholdProperties) {
        this.alarmThresholdProperties = alarmThresholdProperties;
    }

    //@JmsListener(destination = "sensorDataQueue")
    public void receiveSensorData(String jsonString) throws JsonProcessingException {
        SensorData parsedSensorData = fromJson(jsonString);
        checkThreshold(parsedSensorData);
    }
    @PostConstruct
    public void loadAlarmThresholdProperties(){
        thresholds = alarmThresholdProperties.getThresholds();
    }

    private void checkThreshold(SensorData sensorData) {

        List<AlarmThresholdProperties.AlarmThreshold> triggeredSensors = thresholds.stream().filter(threshold -> sensorData.getSensorType().equals(threshold.getType()) &&
                        sensorData.getSensorValue() > threshold.getValue())
                .toList();

        triggeredSensors.forEach(threshold -> logger.warn("Warehouse Name: {}, Warehouse Ip: {}, Sensor Id: {} Type: {} Value: {} Threshold value {} exceeded!",
                sensorData.getWarehouseName(),
                sensorData.getWarehouseIp(),
                sensorData.getSensorID(),
                threshold.getType(),
                sensorData.getSensorValue(),
                threshold.getValue()));
    }
}
