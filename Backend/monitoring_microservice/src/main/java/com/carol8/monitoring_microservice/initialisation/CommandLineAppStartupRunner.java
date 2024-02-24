package com.carol8.monitoring_microservice.initialisation;

import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTOList;
import com.carol8.monitoring_microservice.service.DeviceListenerManagerService;
import com.carol8.monitoring_microservice.service.DeviceQueueService;
import com.carol8.monitoring_microservice.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final DeviceQueueService deviceQueueService;
    private final DeviceListenerManagerService deviceListenerManagerService;
    private final NotificationServiceImpl notificationService;
    @Override
    public void run(String...args){
//        Timer t = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                notificationService.sendNotification("jkashdjksadhjka");
//                System.out.println("Notified with jkashdjksadhjka");
//            }
//        };
//        t.scheduleAtFixedRate(tt, 0, 1000);
        DeviceQueueDTOList deviceQueueDTOList = deviceQueueService.getDeviceQueues();
        deviceQueueDTOList.getDeviceQueueDTOS()
            .forEach(deviceQueueDTO ->
                deviceListenerManagerService.createListener(deviceQueueDTO.getQueueName()
            )
        );
    }
}