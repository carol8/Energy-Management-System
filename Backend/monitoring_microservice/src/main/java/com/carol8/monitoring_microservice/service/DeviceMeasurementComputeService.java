package com.carol8.monitoring_microservice.service;

import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasurementDTO;
import com.carol8.monitoring_microservice.enums.DeviceMeasurementOrder;

import java.util.Map;

public interface DeviceMeasurementComputeService {
    Map<DeviceMeasurementOrder, Double> computeMeasurement(DeviceLastMeasurementDTO lastMeasurementDTO, DeviceLastMeasurementDTO measurementDTO);
}
