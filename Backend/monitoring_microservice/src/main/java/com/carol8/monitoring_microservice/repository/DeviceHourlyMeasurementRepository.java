package com.carol8.monitoring_microservice.repository;

import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurement;
import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurementId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DeviceHourlyMeasurementRepository extends JpaRepository<DeviceHourlyMeasurement, DeviceHourlyMeasurementId> {
    List<DeviceHourlyMeasurement> findAllByUuidAndTimestampBetween(UUID uuid, LocalDateTime start, LocalDateTime end);
    List<DeviceHourlyMeasurement> findAllByUuid(UUID uuid);
    void deleteAllByUuid(UUID uuid);
}
