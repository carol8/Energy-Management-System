package com.carol8.device_microservice.service.impl;

import com.carol8.device_microservice.dto.device.*;
import com.carol8.device_microservice.entity.Device;
import com.carol8.device_microservice.enums.DeviceEvent;
import com.carol8.device_microservice.enums.DeviceEventType;
import com.carol8.device_microservice.mapper.DeviceMapper;
import com.carol8.device_microservice.repository.DeviceRepository;
import com.carol8.device_microservice.service.DeviceService;
import com.carol8.device_microservice.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;
    private final Gson gson;
    @Value("${rabbitmq.device-event-queue}")
    private String deviceEventQueue;

    private boolean userUuidExists(UUID uuid){
        return userService.getUsers()
                .getUserDTOs()
                .stream()
                .anyMatch(userDTO -> userDTO.getUuid().equals(uuid));
    }

    @Override
    public DeviceDTOList getDevices() {
        return deviceMapper.toDeviceDTOList(deviceRepository.findAll());
    }

    @Override
    public Optional<DeviceDTOList> getDevicesForUserUuid(UUID userUuid) {
        return deviceRepository.findAllByUserUuid(userUuid)
                .map(deviceMapper::toDeviceDTOList);
    }

    @Override
    public Optional<DeviceDTO> getDevice(UUID uuid) {
        return deviceRepository.findById(uuid)
                .map(deviceMapper::toDeviceDTO);
    }

    @Override
    public Optional<DeviceDTO> createDevice(DeviceCreateDTO dto) {
        Device device = deviceMapper.toDevice(dto);
        if(userUuidExists(dto.getUserUuid())){
            device = deviceRepository.save(device);
            rabbitTemplate.convertAndSend(deviceEventQueue,
                    gson.toJson(deviceMapper.toDeviceEventDTO(device, DeviceEvent.CREATE)),
                    m -> {
                        m.getMessageProperties().setType(DeviceEventType.NORMAL.name());
                        return m;
                    }
            );
            return Optional.of(deviceMapper.toDeviceDTO(device));
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeviceDTO> updateDevice(UUID uuid, DeviceUpdateDTO dto) {
        Optional<Device> deviceOptional = deviceRepository.findById(uuid);
        if(deviceOptional.isPresent()){
            Device updatedDevice = deviceMapper.updateDeviceFromDTO(deviceOptional.get(), dto);
            if(userUuidExists(updatedDevice.getUserUuid())) {
                deviceRepository.save(updatedDevice);
                rabbitTemplate.convertAndSend(deviceEventQueue,
                        gson.toJson(deviceMapper.toDeviceEventDTO(updatedDevice, DeviceEvent.UPDATE)),
                        m -> {
                            m.getMessageProperties().setType(DeviceEventType.NORMAL.name());
                            return m;
                        }
                );
                return Optional.of(deviceMapper.toDeviceDTO(updatedDevice));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeviceDTO> deleteDevice(UUID uuid) {
        Optional<Device> deviceOptional = deviceRepository.findById(uuid);
        if(deviceOptional.isPresent() && userUuidExists(deviceOptional.get().getUserUuid())){
            Device device = deviceOptional.get();
            deviceRepository.delete(device);
            rabbitTemplate.convertAndSend(deviceEventQueue,
                    gson.toJson(deviceMapper.toDeviceEventDTO(device, DeviceEvent.DELETE)),
                    m -> {
                        m.getMessageProperties().setType(DeviceEventType.NORMAL.name());
                        return m;
                    }
            );
            return Optional.of(deviceMapper.toDeviceDTO(device));
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeviceDTOList> deleteDevicesAndUser(UUID userUuid){
        Optional<List<Device>> deviceListOptional = deviceRepository.findAllByUserUuid(userUuid);
        if(deviceListOptional.isPresent() && userUuidExists(userUuid)){
            List<Device> deviceList = deviceListOptional.get();
            deviceRepository.deleteAll(deviceList);
            userService.deleteUser(userUuid);
            deviceList.forEach(device -> rabbitTemplate.convertAndSend(
                    deviceEventQueue,
                    gson.toJson(deviceMapper.toDeviceEventDTO(device, DeviceEvent.DELETE))
            ));
            return Optional.of(deviceMapper.toDeviceDTOList(deviceList));
        }
        return Optional.empty();
    }
}
