package com.carol8.monitoring_microservice.repository;

import com.carol8.monitoring_microservice.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceQueueRepository extends JpaRepository<Device, UUID> {
}
