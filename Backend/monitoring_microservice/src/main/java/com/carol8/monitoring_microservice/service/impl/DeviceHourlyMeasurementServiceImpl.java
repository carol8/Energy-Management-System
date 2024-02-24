package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementCreateDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTOList;
import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementValueUpdateDTO;
import com.carol8.monitoring_microservice.dto.graph.GraphGetDataDTO;
import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurement;
import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurementId;
import com.carol8.monitoring_microservice.mapper.DeviceHourlyMeasurementMapper;
import com.carol8.monitoring_microservice.repository.DeviceHourlyMeasurementRepository;
import com.carol8.monitoring_microservice.service.DeviceHourlyMeasurementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

@Service
@RequiredArgsConstructor
@Log
public class DeviceHourlyMeasurementServiceImpl implements DeviceHourlyMeasurementService {
    private final DeviceHourlyMeasurementRepository deviceHourlyMeasurementRepository;
    private final DeviceHourlyMeasurementMapper deviceHourlyMeasurementMapper;

    @Override
    public Optional<DeviceHourlyMeasurementDTO> getDeviceHourlyMeasurement(DeviceHourlyMeasurementId id) {
        return deviceHourlyMeasurementRepository.findById(id)
                .map(deviceHourlyMeasurementMapper::toDeviceHourlyMeasurementDTO);
    }

    @Override
    public DeviceHourlyMeasurementDTOList getDeviceHourlyMeasurements(GraphGetDataDTO dto) {
        LocalDateTime startLocalDateTime = LocalDateTime.of(dto.getDate(), LocalTime.MIN);
        LocalDateTime endLocalDateTime = LocalDateTime.of(dto.getDate().plusDays(1), LocalTime.MIN);
        DeviceHourlyMeasurementDTOList dtoList = deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementDTOList(
                deviceHourlyMeasurementRepository.findAllByUuidAndTimestampBetween(dto.getDeviceUuid(), startLocalDateTime, endLocalDateTime),
                dto.getUserUuid()
        );
        for (LocalDateTime localDateTime = startLocalDateTime; localDateTime.isBefore(endLocalDateTime); localDateTime = localDateTime.plusHours(1)) {
            LocalDateTime finalLocalDateTime = localDateTime;
            if (dtoList.getDeviceHourlyMeasurementDTOS().stream().noneMatch(deviceMeasurement -> deviceMeasurement.getTimestamp().equals(finalLocalDateTime))) {
                dtoList.getDeviceHourlyMeasurementDTOS().add(DeviceHourlyMeasurementDTO.builder()
                        .uuid(dto.getDeviceUuid())
                        .timestamp(localDateTime)
                        .totalEnergyConsumption(-1.0)
                        .build()
                );
            }
        }
        Collections.sort(dtoList.getDeviceHourlyMeasurementDTOS());

        return dtoList;
    }

    @Override
    public Optional<DeviceHourlyMeasurementDTO> createDeviceHourlyMeasurement(DeviceHourlyMeasurementCreateDTO dto) {
        if (!deviceHourlyMeasurementRepository.existsById(deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementId(dto))) {
            DeviceHourlyMeasurement deviceHourlyMeasurement = deviceHourlyMeasurementRepository.save(deviceHourlyMeasurementMapper.toDeviceHourlyMeasurement(dto));
            return Optional.of(deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementDTO(deviceHourlyMeasurement));
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeviceHourlyMeasurementDTO> updateDeviceHourlyMeasurementValue(DeviceHourlyMeasurementValueUpdateDTO dto) {
        Optional<DeviceHourlyMeasurement> deviceHourlyMeasurementOptional = deviceHourlyMeasurementRepository.findById(
                deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementId(dto)
        );
        if (deviceHourlyMeasurementOptional.isPresent()) {
            DeviceHourlyMeasurement deviceHourlyMeasurement = deviceHourlyMeasurementOptional.get();
            DeviceHourlyMeasurement updatedDeviceHourlyMeasurement = deviceHourlyMeasurementRepository.save(DeviceHourlyMeasurement.builder()
                    .uuid(deviceHourlyMeasurement.getUuid())
                    .timestamp(deviceHourlyMeasurement.getTimestamp())
                    .totalEnergyConsumption(deviceHourlyMeasurement.getTotalEnergyConsumption() + dto.getEnergyConsumption())
                    .build()
            );
            return Optional.of(deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementDTO(updatedDeviceHourlyMeasurement));
        }
        return Optional.empty();
    }

    @Override
    public DeviceHourlyMeasurementDTOList deleteDeviceHourlyMeasurements(UUID uuid, UUID userUuid) {
        List<DeviceHourlyMeasurement> deviceHourlyMeasurementList = deviceHourlyMeasurementRepository.findAllByUuid(uuid);
        if (!deviceHourlyMeasurementList.isEmpty()) {
            deviceHourlyMeasurementRepository.deleteAllByUuid(uuid);
        }
        return deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementDTOList(deviceHourlyMeasurementList, userUuid);
    }
}
