package com.alexm.wh.monitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sensor.type.alarm")
public class AlarmThresholdProperties {

    private List<AlarmThreshold> thresholds;

    public List<AlarmThreshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<AlarmThreshold> thresholds) {
        this.thresholds = thresholds;
    }

    public static class AlarmThreshold {
        private String type;
        private float value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }
}