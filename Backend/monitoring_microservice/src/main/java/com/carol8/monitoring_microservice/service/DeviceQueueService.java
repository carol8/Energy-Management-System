package com.carol8.monitoring_microservice.service;

import com.carol8.monitoring_microservice.dto.device.DeviceQueueCreateDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueCreateDTOList;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTOList;
import com.carol8.monitoring_microservice.entity.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceQueueService {
    Optional<DeviceQueueDTO> getDeviceQueue(UUID uuid);
    DeviceQueueDTOList getDeviceQueues();
    Optional<DeviceQueueDTO> createDeviceQueue(DeviceQueueCreateDTO dto);
    DeviceQueueDTOList createMultipleDeviceQueues(DeviceQueueCreateDTOList dtoList);
    Optional<DeviceQueueDTO> updateDeviceQueue(DeviceQueueCreateDTO dto);
    Optional<DeviceQueueDTO> deleteDeviceQueue(UUID uuid);
    DeviceQueueDTOList deleteAllDeviceQueues();
}
