package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.dto.device.DeviceQueueCreateDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueCreateDTOList;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTOList;
import com.carol8.monitoring_microservice.entity.Device;
import com.carol8.monitoring_microservice.mapper.DeviceQueueMapper;
import com.carol8.monitoring_microservice.repository.DeviceQueueRepository;
import com.carol8.monitoring_microservice.service.DeviceHourlyMeasurementService;
import com.carol8.monitoring_microservice.service.DeviceLastMeasurementService;
import com.carol8.monitoring_microservice.service.DeviceQueueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceQueueServiceImpl implements DeviceQueueService {
    private final DeviceQueueRepository deviceQueueRepository;
    private final DeviceQueueMapper deviceQueueMapper;
    private final DeviceLastMeasurementService deviceLastMeasurementService;
    private final DeviceHourlyMeasurementService deviceHourlyMeasurementService;

    @Override
    public Optional<DeviceQueueDTO> getDeviceQueue(UUID uuid) {
        return deviceQueueRepository.findById(uuid).map(deviceQueueMapper::toDeviceQueueDTO);
    }

    @Override
    public DeviceQueueDTOList getDeviceQueues() {
        return deviceQueueMapper.toDeviceQueueNameDTOList(deviceQueueRepository.findAll());
    }

    @Override
    public Optional<DeviceQueueDTO> createDeviceQueue(DeviceQueueCreateDTO dto) {
        if(!deviceQueueRepository.existsById(dto.getQueueName())){
            Device device = deviceQueueRepository.save(deviceQueueMapper.toDeviceQueue(dto));
            return Optional.of(deviceQueueMapper.toDeviceQueueDTO(device));
        }
        return Optional.empty();
    }

    @Override
    public DeviceQueueDTOList createMultipleDeviceQueues(DeviceQueueCreateDTOList dtoList) {
        List<Device> deviceList = deviceQueueRepository.findAll();
        List<Device> newDeviceListMeasurement = deviceQueueMapper.toDeviceQueueNameList(dtoList);
        newDeviceListMeasurement.removeAll(deviceList);
        List<Device> savedDeviceListMeasurement = deviceQueueRepository.saveAll(newDeviceListMeasurement);
        return deviceQueueMapper.toDeviceQueueNameDTOList(savedDeviceListMeasurement);
    }

    @Override
    public Optional<DeviceQueueDTO> updateDeviceQueue(DeviceQueueCreateDTO dto) {
        Optional<Device> deviceOptional = deviceQueueRepository.findById(dto.getQueueName());
        if(deviceOptional.isPresent()){
            Device updatedDevice = deviceQueueMapper.updateDeviceFromDTO(deviceOptional.get(), dto);
            deviceQueueRepository.save(updatedDevice);
            return Optional.of(deviceQueueMapper.toDeviceQueueDTO(updatedDevice));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<DeviceQueueDTO> deleteDeviceQueue(UUID uuid) {
        Optional<Device> deviceQueueNameOptional = deviceQueueRepository.findById(uuid);
        if(deviceQueueNameOptional.isPresent()){
            Device device = deviceQueueNameOptional.get();
            deviceQueueRepository.delete(device);
            deviceLastMeasurementService.deleteDeviceLastMeasurement(device.getUuid());
            deviceHourlyMeasurementService.deleteDeviceHourlyMeasurements(device.getUuid(), device.getUserUuid());
            return Optional.of(deviceQueueMapper.toDeviceQueueDTO(device));
        }
        return Optional.empty();
    }

    @Override
    public DeviceQueueDTOList deleteAllDeviceQueues() {
        List<Device> deviceList = deviceQueueRepository.findAll();
        deviceQueueRepository.deleteAll();
        return deviceQueueMapper.toDeviceQueueNameDTOList(deviceList);
    }
}
