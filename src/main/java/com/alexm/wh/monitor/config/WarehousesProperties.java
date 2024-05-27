package com.alexm.wh.monitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
public class WarehousesProperties {

    private List<Warehouse> warehouses;

    // Getters and setters

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public static class Warehouse {
        private String name;
        private String ip;
        private List<Sensor> sensors;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public List<Sensor> getSensors() {
            return sensors;
        }

        public void setSensors(List<Sensor> sensors) {
            this.sensors = sensors;
        }
    }

    public static class Sensor {
        private String type;
        private int port;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}