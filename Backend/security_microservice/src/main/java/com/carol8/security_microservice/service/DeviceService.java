package com.carol8.security_microservice.service;

import com.carol8.security_microservice.dto.device.DeviceCreateDTO;
import com.carol8.security_microservice.dto.device.DeviceDTO;
import com.carol8.security_microservice.dto.device.DeviceDTOList;
import com.carol8.security_microservice.dto.device.DeviceUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface DeviceService {
    ResponseEntity<DeviceDTOList> getDevices() throws ExecutionException, InterruptedException;
    ResponseEntity<DeviceDTOList> getDevicesForUserUuid(UUID userUuid) throws ExecutionException, InterruptedException;
    ResponseEntity<DeviceDTO> getDevice(UUID uuid) throws ExecutionException, InterruptedException;
    ResponseEntity<DeviceDTO> createDevice(DeviceCreateDTO dto) throws ExecutionException, InterruptedException;
    ResponseEntity<DeviceDTO> updateDevice(UUID uuid, DeviceUpdateDTO dto) throws ExecutionException, InterruptedException;
    ResponseEntity<DeviceDTO> deleteDevice(UUID uuid) throws ExecutionException, InterruptedException;
    ResponseEntity<DeviceDTOList> deleteDevicesAndUser(UUID userUuid) throws ExecutionException, InterruptedException;
}
