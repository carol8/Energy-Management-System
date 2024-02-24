package com.carol8.monitoring_microservice.controller;

import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTOList;
import com.carol8.monitoring_microservice.dto.graph.GraphGetDataDTO;
import com.carol8.monitoring_microservice.dto.graph.GraphRequestDTO;
import com.carol8.monitoring_microservice.entity.Device;
import com.carol8.monitoring_microservice.entity.DeviceHourlyMeasurement;
import com.carol8.monitoring_microservice.service.DeviceHourlyMeasurementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.logging.Level;

@Configuration
@Controller
@RequiredArgsConstructor
@Log
public class WebSocketController {
    private final DeviceHourlyMeasurementService deviceHourlyMeasurementService;

    @MessageMapping("/graph")
    @SendTo("/topic/graphData")
    public DeviceHourlyMeasurementDTOList handleGraphDataRequest(@Payload GraphRequestDTO dto){
        return deviceHourlyMeasurementService.getDeviceHourlyMeasurements(
                GraphGetDataDTO.builder()
                        .date(LocalDate.parse(dto.getDate()))
                        .deviceUuid(dto.getDeviceUuid())
                        .userUuid(dto.getUserUuid())
                        .build()
        );
    }
}
