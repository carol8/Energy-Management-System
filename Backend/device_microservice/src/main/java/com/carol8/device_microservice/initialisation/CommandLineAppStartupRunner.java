package com.carol8.device_microservice.initialisation;

import com.carol8.device_microservice.dto.device.DeviceDTOList;
import com.carol8.device_microservice.dto.device.DeviceQueueCreateDTOList;
import com.carol8.device_microservice.enums.DeviceEventType;
import com.carol8.device_microservice.mapper.DeviceMapper;
import com.carol8.device_microservice.service.DeviceService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;
    private final RabbitTemplate rabbitTemplate;
    private final Gson gson;

    @Value("${rabbitmq.device-event-queue}")
    private String deviceInitialisationQueue;
    @Override
    public void run(String...args){
        DeviceDTOList deviceDTOList = deviceService.getDevices();
        DeviceQueueCreateDTOList deviceQueueCreateDTOList = deviceMapper.toDeviceQueueCreateDTOList(deviceDTOList);
        rabbitTemplate.convertAndSend(deviceInitialisationQueue,
                gson.toJson(deviceQueueCreateDTOList),
                m -> {
                    m.getMessageProperties().setType(DeviceEventType.BULK.name());
                    return m;
                }
        );
    }
}