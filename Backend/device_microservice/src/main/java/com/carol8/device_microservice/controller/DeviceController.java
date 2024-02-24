package com.carol8.device_microservice.controller;

import com.carol8.device_microservice.dto.device.DeviceCreateDTO;
import com.carol8.device_microservice.dto.device.DeviceDTO;
import com.carol8.device_microservice.dto.device.DeviceDTOList;
import com.carol8.device_microservice.dto.device.DeviceUpdateDTO;
import com.carol8.device_microservice.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping
    ResponseEntity<DeviceDTOList> getDevices(@RequestParam Optional<UUID> userUuid){
        if(userUuid.isPresent()){
            Optional<DeviceDTOList> deviceDTOListOptional = deviceService.getDevicesForUserUuid(userUuid.get());
            return deviceDTOListOptional
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        }
        else{
            DeviceDTOList deviceDTOList = deviceService.getDevices();
            return ResponseEntity.ok(deviceDTOList);
        }
    }

    @GetMapping("/{uuid}")
    ResponseEntity<DeviceDTO> getDevice(@PathVariable UUID uuid){
        Optional<DeviceDTO> deviceDTOOptional = deviceService.getDevice(uuid);
        return deviceDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping
    ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceCreateDTO dto){
        Optional<DeviceDTO> deviceDTOOptional = deviceService.createDevice(dto);
        return deviceDTOOptional
                .map(deviceDTO -> ResponseEntity.status(HttpStatus.CREATED).body(deviceDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PatchMapping("/{uuid}")
    ResponseEntity<DeviceDTO> updateDevice(@PathVariable UUID uuid, @RequestBody DeviceUpdateDTO dto){
        Optional<DeviceDTO> deviceDTOOptional = deviceService.updateDevice(uuid, dto);
        return deviceDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/{uuid}")
    ResponseEntity<DeviceDTO> deleteDevice(@PathVariable UUID uuid){
        Optional<DeviceDTO> deviceDTOOptional = deviceService.deleteDevice(uuid);
        return deviceDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
    @DeleteMapping
    ResponseEntity<DeviceDTOList> deleteDevicesForUser(@RequestParam UUID userUuid){
        Optional<DeviceDTOList> deviceDTOOptional = deviceService.deleteDevicesAndUser(userUuid);
        return deviceDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }
}
