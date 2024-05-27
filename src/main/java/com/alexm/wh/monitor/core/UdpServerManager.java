package com.alexm.wh.monitor.core;

import com.alexm.wh.monitor.config.WarehousesProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.udp.UdpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class UdpServerManager {
    @Autowired
    private WarehousesProperties warehousesProperties;

    @Autowired
    private SensorDataHandler sensorDataHandler;

    private static final Logger logger = LoggerFactory.getLogger(UdpServerManager.class);

    @PostConstruct
    public void startServers() {
        List<WarehousesProperties.Warehouse> warehouses = warehousesProperties.getWarehouses();
        warehouses.forEach(warehouse -> startUdpServer(warehouse.getName(),
                warehouse.getIp(),
                warehouse.getSensors()));
    }

    private void startUdpServer(String name, String ip, List<WarehousesProperties.Sensor> sensors) {
        logger.info("Warehouse {} , IP {} - Start UDP sensor listeners.", name, ip);
        sensors.forEach(sensor -> startUdpServer(name, ip,
                sensor.getPort(),
                sensor.getType()));
    }

    private void startUdpServer(String name, String ip, int port, String sensorType) {
        UdpServer.create()
                .host(ip)
                .port(port)
                .handle((inbound, outbound) -> inbound.receive()
                        .asString()
                        .doOnNext(data -> {
                            try {
                                sensorDataHandler.handleSensorData(name, ip, sensorType, data);
                            } catch (JsonProcessingException e) {
                                logger.error("Cannot parse sensor data", e);
                            }
                        })
                        .then(Mono.never())) // Keep the connection open to receive more messages
                .bind()
                .doOnSuccess(conn -> logger.info("Warehouse {} , IP {} - {} Sensor UDP listener started on port {}", name, ip, sensorType, port))
                .doOnError(error -> logger.error("Warehouse {} , IP {} - Error starting {} Sensor UDP listener on port {}", name, ip, sensorType, port, error))
                .subscribe();
    }


}
