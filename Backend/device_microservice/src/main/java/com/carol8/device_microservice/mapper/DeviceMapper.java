package com.carol8.device_microservice.mapper;

import com.carol8.device_microservice.dto.device.*;
import com.carol8.device_microservice.entity.Device;
import com.carol8.device_microservice.enums.DeviceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeviceMapper {
    public Device toDevice(DeviceCreateDTO dto) {
        return Device.builder()
                .userUuid(dto.getUserUuid())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .maxWh(dto.getMaxWh())
                .build();
    }

    public DeviceDTO toDeviceDTO(Device device) {
        return DeviceDTO.builder()
                .uuid(device.getUuid())
                .userUuid(device.getUserUuid())
                .description(device.getDescription())
                .address(device.getAddress())
                .maxWh(device.getMaxWh())
                .build();
    }

    public DeviceDTOList toDeviceDTOList(List<Device> deviceList) {
        return DeviceDTOList.builder()
                .deviceDTOs(deviceList.stream()
                        .map(this::toDeviceDTO)
                        .toList())
                .build();
    }

    public DeviceEventDTO toDeviceEventDTO(Device device, DeviceEvent deviceEvent) {
        return DeviceEventDTO.builder()
                .deviceQueueCreateDTO(toDeviceQueueCreateDTO(toDeviceDTO(device)))
                .deviceEvent(deviceEvent)
                .build();
    }

    public Device updateDeviceFromDTO(Device device, DeviceUpdateDTO dto) {
        if (dto.getUserUuid() != null) {
            device.setUserUuid(dto.getUserUuid());
        }
        if (!dto.getDescription().isEmpty()) {
            device.setDescription(dto.getDescription());
        }
        if (!dto.getAddress().isEmpty()) {
            device.setAddress(dto.getAddress());
        }
        if (dto.getMaxWh() != 0) {
            device.setMaxWh(dto.getMaxWh());
        }
        return device;
    }

    public DeviceQueueCreateDTO toDeviceQueueCreateDTO(DeviceDTO dto) {
        return DeviceQueueCreateDTO.builder()
                .queueName(dto.getUuid())
                .userUuid(dto.getUserUuid())
                .maxWh(dto.getMaxWh())
                .build();
    }

    public DeviceQueueCreateDTOList toDeviceQueueCreateDTOList(DeviceDTOList dtoList) {
        return DeviceQueueCreateDTOList.builder()
                .deviceQueueCreateDTOS(dtoList.getDeviceDTOs().stream()
                        .map(this::toDeviceQueueCreateDTO)
                        .toList())
                .build();
    }
}
