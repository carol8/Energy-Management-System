package com.carol8.monitoring_microservice.service;

import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementCreateDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTOList;
import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementValueUpdateDTO;
import com.carol8.monitoring_microservice.dto.graph.GraphGetDataDTO;
import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurementId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceHourlyMeasurementService {
    Optional<DeviceHourlyMeasurementDTO> getDeviceHourlyMeasurement(DeviceHourlyMeasurementId id);
    DeviceHourlyMeasurementDTOList getDeviceHourlyMeasurements(GraphGetDataDTO dto);
    Optional<DeviceHourlyMeasurementDTO> createDeviceHourlyMeasurement(DeviceHourlyMeasurementCreateDTO dto);
    Optional<DeviceHourlyMeasurementDTO> updateDeviceHourlyMeasurementValue(DeviceHourlyMeasurementValueUpdateDTO dto);
    DeviceHourlyMeasurementDTOList deleteDeviceHourlyMeasurements(UUID uuid, UUID userUuid);
}
