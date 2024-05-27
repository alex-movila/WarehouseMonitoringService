package com.alexm.wh.monitor;

import com.alexm.wh.monitor.config.AlarmThresholdProperties;
import com.alexm.wh.monitor.config.WarehousesProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableConfigurationProperties({WarehousesProperties.class, AlarmThresholdProperties.class})
public class WarehouseMonitoringApp {

    public static void main(String[] args) {

        SpringApplication.run(WarehouseMonitoringApp.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            // Keep the application running
            keepApplicationRunning();
        };
    }


   private void keepApplicationRunning() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}