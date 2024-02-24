package com.carol8.monitoring_microservice.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceHourlyMeasurementDTOList {
    UUID userUuid;
    List<DeviceHourlyMeasurementDTO> deviceHourlyMeasurementDTOS;
}
