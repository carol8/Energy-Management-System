package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTO;
import com.carol8.monitoring_microservice.service.NotificationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                    context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();

    public void sendNotification(UUID userUuid, DeviceHourlyMeasurementDTO deviceHourlyMeasurementDTO) {
        messagingTemplate.convertAndSend(
                "/topic/notifications",
                gson.toJson(deviceHourlyMeasurementDTO),
                Map.of("userUuid", userUuid.toString())
        );
    }
}