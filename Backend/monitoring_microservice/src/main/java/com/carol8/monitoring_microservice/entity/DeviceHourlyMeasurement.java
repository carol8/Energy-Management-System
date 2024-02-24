package com.carol8.monitoring_microservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_hourly_measurement")
@IdClass(DeviceHourlyMeasurementId.class)
public class DeviceHourlyMeasurement {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Id
    private LocalDateTime timestamp;

    private Double totalEnergyConsumption;
}
