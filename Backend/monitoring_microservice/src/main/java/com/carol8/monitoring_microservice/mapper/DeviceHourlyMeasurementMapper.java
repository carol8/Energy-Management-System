package com.carol8.monitoring_microservice.mapper;

import com.carol8.monitoring_microservice.dto.device.*;
import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurement;
import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurementId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeviceHourlyMeasurementMapper {
    public DeviceHourlyMeasurement toDeviceHourlyMeasurement(DeviceHourlyMeasurementCreateDTO dto) {
        return DeviceHourlyMeasurement.builder()
                .uuid(dto.getUuid())
                .timestamp(dto.getTimestamp().withMinute(0).withSecond(0).withNano(0))
                .totalEnergyConsumption(dto.getInitialEnergyConsumption())
                .build();
    }

    public DeviceHourlyMeasurementDTO toDeviceHourlyMeasurementDTO(DeviceHourlyMeasurement deviceHourlyMeasurement) {
        return DeviceHourlyMeasurementDTO.builder()
                .uuid(deviceHourlyMeasurement.getUuid())
                .timestamp(deviceHourlyMeasurement.getTimestamp())
                .totalEnergyConsumption(deviceHourlyMeasurement.getTotalEnergyConsumption())
                .build();
    }

    public DeviceHourlyMeasurementCreateDTO toDeviceHourlyMeasurementCreateDTO(DeviceLastMeasurementDTO dto) {
        return DeviceHourlyMeasurementCreateDTO.builder()
                .uuid(dto.getUuid())
                .timestamp(dto.getLastDateTime().withMinute(0).withSecond(0).withNano(0))
                .initialEnergyConsumption(dto.getLastMeasurement())
                .build();
    }

    public DeviceHourlyMeasurementValueUpdateDTO toDeviceHourlyMeasurementValueUpdateDTO(UUID uuid, LocalDateTime timestamp, Double energyConsumption) {
        return DeviceHourlyMeasurementValueUpdateDTO.builder()
                .uuid(uuid)
                .timestamp(timestamp.withMinute(0).withSecond(0).withNano(0))
                .energyConsumption(energyConsumption)
                .build();
    }

    public DeviceHourlyMeasurementId toDeviceHourlyMeasurementId(DeviceHourlyMeasurementCreateDTO dto) {
        return DeviceHourlyMeasurementId.builder()
                .uuid(dto.getUuid())
                .timestamp(dto.getTimestamp().withMinute(0).withSecond(0).withNano(0))
                .build();
    }

    public DeviceHourlyMeasurementId toDeviceHourlyMeasurementId(DeviceHourlyMeasurementValueUpdateDTO dto) {
        return DeviceHourlyMeasurementId.builder()
                .uuid(dto.getUuid())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public DeviceHourlyMeasurementId toDeviceHourlyMeasurementId(UUID uuid, LocalDateTime dateTime) {
        return DeviceHourlyMeasurementId.builder()
                .uuid(uuid)
                .timestamp(dateTime.withMinute(0).withSecond(0).withNano(0))
                .build();
    }

    public DeviceHourlyMeasurementDTOList toDeviceHourlyMeasurementDTOList(List<DeviceHourlyMeasurement> deviceHourlyMeasurementList, UUID userUuid) {
        return DeviceHourlyMeasurementDTOList.builder()
                .userUuid(userUuid)
                .deviceHourlyMeasurementDTOS(deviceHourlyMeasurementList.stream()
                        .map(this::toDeviceHourlyMeasurementDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
