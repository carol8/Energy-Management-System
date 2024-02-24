package com.carol8.monitoring_microservice.service;

import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTO;

import java.util.UUID;

public interface NotificationService {
    void sendNotification(UUID userUuid, DeviceHourlyMeasurementDTO deviceHourlyMeasurementDTO);
}
