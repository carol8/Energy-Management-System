package com.carol8.monitoring_microservice.service;

import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasurementDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasurementUpdateDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasuremetCreateDTO;

import java.util.Optional;
import java.util.UUID;

public interface DeviceLastMeasurementService {
    Optional<DeviceLastMeasurementDTO> getDeviceLastMeasurement(UUID uuid);
    Optional<DeviceLastMeasurementDTO> createDeviceLastMeasurement(DeviceLastMeasuremetCreateDTO dto);
    Optional<DeviceLastMeasurementDTO> updateDeviceLastMeasurement(UUID uuid, DeviceLastMeasurementUpdateDTO dto);
    Optional<DeviceLastMeasurementDTO> deleteDeviceLastMeasurement(UUID uuid);
}
