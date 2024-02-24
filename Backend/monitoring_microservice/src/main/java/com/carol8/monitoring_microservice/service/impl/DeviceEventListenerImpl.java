package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.dto.device.*;
import com.carol8.monitoring_microservice.enums.DeviceEventType;
import com.carol8.monitoring_microservice.service.DeviceListenerManagerService;
import com.carol8.monitoring_microservice.service.DeviceQueueService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceEventListenerImpl{
    private final Gson gson;
    private final DeviceQueueService deviceQueueService;
    private final DeviceListenerManagerService deviceListenerManagerService;

    @RabbitListener(queues = "${rabbitmq.device-event-queue}")
    public void onDeviceEventReceived(final Message message) {
        switch (DeviceEventType.valueOf(message.getMessageProperties().getType())){
            case NORMAL:
                DeviceEventDTO deviceEventDTO = gson.fromJson(new String(message.getBody()), DeviceEventDTO.class);
                System.out.println(deviceEventDTO);
                switch (deviceEventDTO.getDeviceEvent()) {
                    case CREATE:
                        createDeviceNameQueue(deviceEventDTO.getDeviceQueueCreateDTO());
                        break;
                    case UPDATE:
                        updateDeviceNameQueue(deviceEventDTO.getDeviceQueueCreateDTO());
                        break;
                    case DELETE:
                        deleteDeviceNameQueue(deviceEventDTO.getDeviceQueueCreateDTO().getQueueName());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + deviceEventDTO.getDeviceEvent());
                }
                break;
            case BULK:
                DeviceQueueCreateDTOList deviceQueueCreateDTOList = gson.fromJson(new String(message.getBody()), DeviceQueueCreateDTOList.class);
                System.out.println(deviceQueueCreateDTOList);
                DeviceQueueDTOList deviceQueueNameDTOListOld = deviceQueueService.deleteAllDeviceQueues();
                deviceQueueNameDTOListOld.getDeviceQueueDTOS()
                        .forEach(deviceQueueDTO -> deviceListenerManagerService.deleteListener(deviceQueueDTO.getQueueName()));
                DeviceQueueDTOList deviceQueueNameDTOList = deviceQueueService.createMultipleDeviceQueues(deviceQueueCreateDTOList);
                deviceQueueNameDTOList.getDeviceQueueDTOS()
                        .forEach(deviceQueueNameDTO -> deviceListenerManagerService.createListener(deviceQueueNameDTO.getQueueName()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + DeviceEventType.valueOf(message.getMessageProperties().getType()));
        }
    }

    private void createDeviceNameQueue(DeviceQueueCreateDTO dto){
        deviceListenerManagerService.createListener(dto.getQueueName());
        deviceQueueService.createDeviceQueue(dto);
    }

    private void updateDeviceNameQueue(DeviceQueueCreateDTO dto){
        deviceQueueService.updateDeviceQueue(dto);
    }

    private void deleteDeviceNameQueue(UUID queueName){
        deviceListenerManagerService.deleteListener(queueName);
        deviceQueueService.deleteDeviceQueue(queueName);
    }
}
