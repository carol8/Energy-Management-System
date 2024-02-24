package com.carol8.device_microservice.service;

import com.carol8.device_microservice.dto.device.*;

import java.util.Optional;
import java.util.UUID;

public interface DeviceService {
    DeviceDTOList getDevices();
    Optional<DeviceDTOList> getDevicesForUserUuid(UUID userUuid);
    Optional<DeviceDTO> getDevice(UUID uuid);
    Optional<DeviceDTO> createDevice(DeviceCreateDTO dto);
    Optional<DeviceDTO> updateDevice(UUID uuid, DeviceUpdateDTO dto);
    Optional<DeviceDTO> deleteDevice(UUID uuid);
    Optional<DeviceDTOList> deleteDevicesAndUser(UUID userUuid);
}
