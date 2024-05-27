package com.alexm.wh.monitor;

import com.alexm.wh.monitor.config.AlarmThresholdProperties;
import com.alexm.wh.monitor.core.CentralMonitoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CentralMonitoringServiceTest {

    @Mock
    private AlarmThresholdProperties alarmThresholdProperties;

    @InjectMocks
    private CentralMonitoringService centralMonitoringService;

    private TestAppender testAppender;

    @BeforeEach
    public void setUp() {
        // Set up the custom appender
        testAppender = new TestAppender();
        testAppender.start();
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(testAppender);
    }

    @Test
    public void testReceiveSensorData_ExceedsThreshold() throws JsonProcessingException {
        // Arrange
        AlarmThresholdProperties.AlarmThreshold threshold = new AlarmThresholdProperties.AlarmThreshold();
        threshold.setType("temperature");
        threshold.setValue(25);

        when(alarmThresholdProperties.getThresholds()).thenReturn(List.of(threshold));
        centralMonitoringService.loadAlarmThresholdProperties();

        String jsonString = "{\"warehouseName\":\"Warehouse1\",\"warehouseIp\":\"127.0.0.1\",\"sensorType\":\"temperature\",\"sensorID\":\"h1\",\"sensorValue\":30}";

        // Act
        centralMonitoringService.receiveSensorData(jsonString);

        // Assert
        assertTrue(testAppender.contains("Warehouse Name: Warehouse1, Warehouse Ip: 127.0.0.1, Sensor Id: h1 Type: temperature Value: 30.0 Threshold value 25.0 exceeded!", Level.WARN));
    }

    @Test
    public void testReceiveSensorData_DoesNotExceedThreshold() throws JsonProcessingException {
        // Arrange
        AlarmThresholdProperties.AlarmThreshold threshold = new AlarmThresholdProperties.AlarmThreshold();
        threshold.setType("temperature");
        threshold.setValue(35);

        when(alarmThresholdProperties.getThresholds()).thenReturn(List.of(threshold));
        centralMonitoringService.loadAlarmThresholdProperties();

        String jsonString = "{\"warehouseName\":\"Warehouse1\",\"warehouseIp\":\"127.0.0.1\",\"sensorType\":\"temperature\",\"sensorID\":\"h1\",\"sensorValue\":30}";

        // Act
        centralMonitoringService.receiveSensorData(jsonString);

        // Assert
        assertTrue(testAppender.logEvents.isEmpty());
    }

    @Test
    public void testCheckThreshold_MultipleThresholds() throws JsonProcessingException {
        // Arrange
        AlarmThresholdProperties.AlarmThreshold threshold1 = new AlarmThresholdProperties.AlarmThreshold();
        threshold1.setType("temperature");
        threshold1.setValue(25);

        AlarmThresholdProperties.AlarmThreshold threshold2 = new AlarmThresholdProperties.AlarmThreshold();
        threshold2.setType("humidity");
        threshold2.setValue(70);

        when(alarmThresholdProperties.getThresholds()).thenReturn(Arrays.asList(threshold1, threshold2));
        centralMonitoringService.loadAlarmThresholdProperties();

        String jsonString = "{\"warehouseName\":\"Warehouse1\",\"warehouseIp\":\"127.0.0.1\",\"sensorType\":\"temperature\",\"sensorID\":\"h1\",\"sensorValue\":30}";


        // Act
        centralMonitoringService.receiveSensorData(jsonString);
        // Assert
        assertTrue(testAppender.contains("Warehouse Name: Warehouse1, Warehouse Ip: 127.0.0.1, Sensor Id: h1 Type: temperature Value: 30.0 Threshold value 25.0 exceeded!", Level.WARN));

    }

    static class TestAppender extends AppenderBase<ILoggingEvent> {
        private final List<ILoggingEvent> logEvents = new ArrayList<>();

        @Override
        protected void append(ILoggingEvent eventObject) {
            logEvents.add(eventObject);
        }

        public boolean contains(String message, Level level) {
            return logEvents.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains(message) && event.getLevel().equals(level));
        }
    }
}
