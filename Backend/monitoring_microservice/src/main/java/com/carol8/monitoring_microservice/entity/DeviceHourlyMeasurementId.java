package com.carol8.monitoring_microservice.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceHourlyMeasurementId implements Serializable {
    private UUID uuid;
    private LocalDateTime timestamp;
}
