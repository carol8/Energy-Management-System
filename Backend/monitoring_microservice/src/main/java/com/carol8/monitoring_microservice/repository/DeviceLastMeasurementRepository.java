package com.carol8.monitoring_microservice.repository;

import com.carol8.monitoring_microservice.entity.DeviceLastMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceLastMeasurementRepository extends JpaRepository<DeviceLastMeasurement, UUID> {
}
