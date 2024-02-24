package com.carol8.monitoring_microservice.mapper;

import com.carol8.monitoring_microservice.dto.device.DeviceQueueCreateDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueCreateDTOList;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTOList;
import com.carol8.monitoring_microservice.entity.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeviceQueueMapper {
    public Device toDeviceQueue(DeviceQueueCreateDTO dto){
        return Device.builder()
                .uuid(dto.getQueueName())
                .userUuid(dto.getUserUuid())
                .maxWh(dto.getMaxWh())
                .build();
    }

    public DeviceQueueDTO toDeviceQueueDTO(Device device){
        return DeviceQueueDTO.builder()
                .queueName(device.getUuid())
                .userUuid(device.getUserUuid())
                .maxWh(device.getMaxWh())
                .build();
    }

    public List<Device> toDeviceQueueNameList(DeviceQueueCreateDTOList dtoList){
        return dtoList.getDeviceQueueCreateDTOS().stream()
                .map(this::toDeviceQueue)
                .collect(Collectors.toList());
    }

    public DeviceQueueDTOList toDeviceQueueNameDTOList(List<Device> deviceList){
        return DeviceQueueDTOList.builder()
                .deviceQueueDTOS(deviceList.stream()
                        .map(this::toDeviceQueueDTO)
                        .toList())
                .build();
    }

    public Device updateDeviceFromDTO(Device device, DeviceQueueCreateDTO dto) {
        if (dto.getUserUuid() != null) {
            device.setUserUuid(dto.getUserUuid());
        }
        if (dto.getMaxWh() != 0) {
            device.setMaxWh(dto.getMaxWh());
        }
        return device;
    }
}
