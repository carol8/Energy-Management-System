package com.carol8.monitoring_microservice.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceHourlyMeasurementDTO implements Comparable<DeviceHourlyMeasurementDTO> {
    private UUID uuid;
    private LocalDateTime timestamp;
    private Double totalEnergyConsumption;

    @Override
    public int compareTo(DeviceHourlyMeasurementDTO o) {
        return this.getTimestamp().compareTo(o.getTimestamp());
    }
}
