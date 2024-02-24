package com.carol8.security_microservice.controller;

import com.carol8.security_microservice.dto.device.DeviceCreateDTO;
import com.carol8.security_microservice.dto.device.DeviceDTO;
import com.carol8.security_microservice.dto.device.DeviceDTOList;
import com.carol8.security_microservice.dto.device.DeviceUpdateDTO;
import com.carol8.security_microservice.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("CallToPrintStackTrace")
@CrossOrigin
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping
    ResponseEntity<?> getDevices(@RequestParam Optional<UUID> userUuid){
        if(userUuid.isPresent()){
            try {
                return deviceService.getDevicesForUserUuid(userUuid.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
            }
        }
        else{
            try {
                return deviceService.getDevices();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
            }
        }
    }

    @GetMapping("/{uuid}")
    ResponseEntity<?> getDevice(@PathVariable UUID uuid){
        try {
            return deviceService.getDevice(uuid);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }

    @PostMapping
    ResponseEntity<?> createDevice(@RequestBody DeviceCreateDTO dto){
        try {
            return deviceService.createDevice(dto);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }

    @PatchMapping("/{uuid}")
    ResponseEntity<?> updateDevice(@PathVariable UUID uuid, @RequestBody DeviceUpdateDTO dto){
        try {
            return deviceService.updateDevice(uuid, dto);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }

    @DeleteMapping("/{uuid}")
    ResponseEntity<?> deleteDevice(@PathVariable UUID uuid){
        try {
            return deviceService.deleteDevice(uuid);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }
    @DeleteMapping
    ResponseEntity<?> deleteDevicesForUser(@RequestParam UUID userUuid){
        try {
            return deviceService.deleteDevicesAndUser(userUuid);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }
}
