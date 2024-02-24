package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.service.DeviceListenerManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceListenerManagerServiceImpl implements DeviceListenerManagerService {
    private final AmqpAdmin amqpAdmin;
    private final SimpleMessageListenerContainer listenerContainer;

    @Override
    public void createListener(UUID queueName) {
        Queue newQueue = new Queue(queueName.toString(), true);
        amqpAdmin.declareQueue(newQueue);
        listenerContainer.addQueues(newQueue);
        System.out.println("Created listener in " + listenerContainer);
    }

    @Override
    public void deleteListener(UUID queueName) {
        amqpAdmin.deleteQueue(queueName.toString());
        listenerContainer.removeQueues(new Queue(queueName.toString()));
        System.out.println("Deleted listener from " + listenerContainer);
    }
}
