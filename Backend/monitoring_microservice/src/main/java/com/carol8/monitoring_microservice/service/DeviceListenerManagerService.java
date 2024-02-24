package com.carol8.monitoring_microservice.service;

import java.util.UUID;

public interface DeviceListenerManagerService {
    void createListener(UUID queueName);
    void deleteListener(UUID queueName);
}
