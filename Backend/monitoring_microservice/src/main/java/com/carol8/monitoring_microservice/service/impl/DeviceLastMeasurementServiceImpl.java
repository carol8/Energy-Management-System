package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasurementDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasurementUpdateDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasuremetCreateDTO;
import com.carol8.monitoring_microservice.entity.DeviceLastMeasurement;
import com.carol8.monitoring_microservice.mapper.DeviceLastMeasurementMapper;
import com.carol8.monitoring_microservice.repository.DeviceLastMeasurementRepository;
import com.carol8.monitoring_microservice.service.DeviceLastMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceLastMeasurementServiceImpl implements DeviceLastMeasurementService {
    private final DeviceLastMeasurementRepository deviceLastMeasurementRepository;
    private final DeviceLastMeasurementMapper deviceLastMeasurementMapper;

    @Override
    public Optional<DeviceLastMeasurementDTO> getDeviceLastMeasurement(UUID uuid) {
        return deviceLastMeasurementRepository.findById(uuid)
                .map(deviceLastMeasurementMapper::toDeviceLastMeasurementDTO);
    }

    @Override
    public Optional<DeviceLastMeasurementDTO> createDeviceLastMeasurement(DeviceLastMeasuremetCreateDTO dto) {
        if(!deviceLastMeasurementRepository.existsById(dto.getUuid())){
            DeviceLastMeasurement deviceLastMeasurement = deviceLastMeasurementRepository.save(deviceLastMeasurementMapper.toDeviceLastMeasurement(dto));
            return Optional.of(deviceLastMeasurementMapper.toDeviceLastMeasurementDTO(deviceLastMeasurement));
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeviceLastMeasurementDTO> updateDeviceLastMeasurement(UUID uuid, DeviceLastMeasurementUpdateDTO dto) {
        Optional<DeviceLastMeasurement> deviceLastMeasurementOptional = deviceLastMeasurementRepository.findById(uuid);
        if(deviceLastMeasurementOptional.isPresent()){
            DeviceLastMeasurement updatedDeviceLastMeasurement = deviceLastMeasurementMapper.updateDeviceLastMeasurementFromDTO(deviceLastMeasurementOptional.get(), dto);
            deviceLastMeasurementRepository.save(updatedDeviceLastMeasurement);
            return Optional.of(deviceLastMeasurementMapper.toDeviceLastMeasurementDTO(updatedDeviceLastMeasurement));
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeviceLastMeasurementDTO> deleteDeviceLastMeasurement(UUID uuid) {
        Optional<DeviceLastMeasurement> deviceLastMeasurementOptional = deviceLastMeasurementRepository.findById(uuid);
        if(deviceLastMeasurementOptional.isPresent()){
            DeviceLastMeasurement deviceLastMeasurement = deviceLastMeasurementOptional.get();
            deviceLastMeasurementRepository.delete(deviceLastMeasurement);
            return Optional.of(deviceLastMeasurementMapper.toDeviceLastMeasurementDTO(deviceLastMeasurement));
        }
        return Optional.empty();
    }
}
