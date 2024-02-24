package com.carol8.monitoring_microservice.mapper;

import com.carol8.monitoring_microservice.dto.device.*;
import com.carol8.monitoring_microservice.entity.DeviceLastMeasurement;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Component
public class DeviceLastMeasurementMapper {
    public DeviceLastMeasurementDTO toDeviceLastMeasurementDTO(DeviceLastMeasurement deviceLastMeasurement){
        return DeviceLastMeasurementDTO.builder()
                .uuid(deviceLastMeasurement.getUuid())
                .lastDateTime(deviceLastMeasurement.getLastDateTime())
                .lastMeasurement(deviceLastMeasurement.getLastMeasurement())
                .build();
    }

    public DeviceLastMeasurementDTO toDeviceLastMeasurementDTO(DeviceMeasurementDTO dto){
        return DeviceLastMeasurementDTO.builder()
                .uuid(dto.getDeviceId())
                .lastDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getTimestamp()), ZoneId.systemDefault()))
                .lastMeasurement(dto.getMeasurementValue())
                .build();
    }

    public DeviceLastMeasurement toDeviceLastMeasurement(DeviceLastMeasuremetCreateDTO dto) {
        return DeviceLastMeasurement.builder()
                .uuid(dto.getUuid())
                .lastDateTime(dto.getLastDateTime())
                .lastMeasurement(dto.getLastMeasurement())
                .build();
    }

    public DeviceLastMeasurement updateDeviceLastMeasurementFromDTO(DeviceLastMeasurement deviceLastMeasurement, DeviceLastMeasurementUpdateDTO dto) {
        if(dto.getUuid() != null){
            deviceLastMeasurement.setUuid(dto.getUuid());
        }
        if(dto.getLastDateTime() != null){
            deviceLastMeasurement.setLastDateTime(dto.getLastDateTime());
        }
        if(dto.getLastMeasurement() != null){
            deviceLastMeasurement.setLastMeasurement(dto.getLastMeasurement());
        }
        return deviceLastMeasurement;
    }

    public DeviceLastMeasuremetCreateDTO toDeviceLastMeasurementCreateDTO(DeviceLastMeasurementDTO dto) {
        return DeviceLastMeasuremetCreateDTO.builder()
                .uuid(dto.getUuid())
                .lastDateTime(dto.getLastDateTime())
                .lastMeasurement(dto.getLastMeasurement())
                .build();
    }

    public DeviceLastMeasurementUpdateDTO toDeviceLastMeasurementUpdateDTO(DeviceLastMeasurementDTO dto) {
        return DeviceLastMeasurementUpdateDTO.builder()
                .uuid(dto.getUuid())
                .lastDateTime(dto.getLastDateTime())
                .lastMeasurement(dto.getLastMeasurement())
                .build();
    }
}
