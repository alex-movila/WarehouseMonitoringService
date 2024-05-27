package com.alexm.wh.monitor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorData {
   private String warehouseName;
   private String warehouseIp;
   private String sensorType;
   private String sensorID;
   private float sensorValue;
}

