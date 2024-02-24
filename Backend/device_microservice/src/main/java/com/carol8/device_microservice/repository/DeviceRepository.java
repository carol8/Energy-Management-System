package com.carol8.device_microservice.repository;

import com.carol8.device_microservice.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    boolean existsByUserUuidAndDescriptionAndAddressAndMaxWh(UUID userUuid, String description, String address, double MaxWh);
    Optional<List<Device>> findAllByUserUuid(UUID userUuid);
}
