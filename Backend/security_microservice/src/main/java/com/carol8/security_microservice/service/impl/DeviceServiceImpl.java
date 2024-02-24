package com.carol8.security_microservice.service.impl;

import com.carol8.security_microservice.dto.device.DeviceCreateDTO;
import com.carol8.security_microservice.dto.device.DeviceDTO;
import com.carol8.security_microservice.dto.device.DeviceDTOList;
import com.carol8.security_microservice.dto.device.DeviceUpdateDTO;
import com.carol8.security_microservice.dto.user.extra.UserExtraDTO;
import com.carol8.security_microservice.service.DeviceService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final WebClient webClient;

    public DeviceServiceImpl(Environment env) {
        this.webClient = WebClient.create("http://" + env.getProperty("devicemicroservice.ip") + ":" + env.getProperty("devicemicroservice.port"));
    }

    @Override
    public ResponseEntity<DeviceDTOList> getDevices() throws ExecutionException, InterruptedException {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/devices")
                        .build())
                .retrieve()
                .toEntity(DeviceDTOList.class)
                .toFuture()
                .get();
    }

    @Override
    public ResponseEntity<DeviceDTOList> getDevicesForUserUuid(UUID userUuid) throws ExecutionException, InterruptedException {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/devices")
                        .queryParam("userUuid", userUuid)
                        .build())
                .retrieve()
                .toEntity(DeviceDTOList.class)
                .toFuture()
                .get();
    }

    @Override
    public ResponseEntity<DeviceDTO> getDevice(UUID uuid) throws ExecutionException, InterruptedException {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/devices/" + uuid.toString())
                        .build())
                .retrieve()
                .toEntity(DeviceDTO.class)
                .toFuture()
                .get();
    }

    @Override
    public ResponseEntity<DeviceDTO> createDevice(DeviceCreateDTO dto) throws ExecutionException, InterruptedException {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/devices")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .retrieve()
                .toEntity(DeviceDTO.class)
                .toFuture()
                .get();
    }

    @Override
    public ResponseEntity<DeviceDTO> updateDevice(UUID uuid, DeviceUpdateDTO dto) throws ExecutionException, InterruptedException {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/devices/" + uuid.toString())
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .retrieve()
                .toEntity(DeviceDTO.class)
                .toFuture()
                .get();
    }

    @Override
    public ResponseEntity<DeviceDTO> deleteDevice(UUID uuid) throws ExecutionException, InterruptedException {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/devices/" + uuid.toString())
                        .build())
                .retrieve()
                .toEntity(DeviceDTO.class)
                .toFuture()
                .get();
    }

    @Override
    public ResponseEntity<DeviceDTOList> deleteDevicesAndUser(UUID userUuid) throws ExecutionException, InterruptedException {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/devices")
                        .queryParam("userUuid", userUuid.toString())
                        .build())
                .retrieve()
                .toEntity(DeviceDTOList.class)
                .toFuture()
                .get();
    }
}
