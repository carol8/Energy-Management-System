package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.dto.device.DeviceHourlyMeasurementDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasurementDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceMeasurementDTO;
import com.carol8.monitoring_microservice.dto.device.DeviceQueueDTO;
import com.carol8.monitoring_microservice.enums.DeviceMeasurementOrder;
import com.carol8.monitoring_microservice.mapper.DeviceHourlyMeasurementMapper;
import com.carol8.monitoring_microservice.mapper.DeviceLastMeasurementMapper;
import com.carol8.monitoring_microservice.service.*;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceMeasurementListenerImpl implements ChannelAwareMessageListener {
    private final Gson gson;
    private final DeviceLastMeasurementService deviceLastMeasurementService;
    private final DeviceLastMeasurementMapper deviceLastMeasurementMapper;
    private final DeviceHourlyMeasurementService deviceHourlyMeasurementService;
    private final DeviceHourlyMeasurementMapper deviceHourlyMeasurementMapper;
    private final DeviceMeasurementComputeService deviceMeasurementComputeService;
    private final DeviceQueueService deviceQueueService;
    private final NotificationService notificationService;

    @Override
    public void onMessage(Message message, Channel channel) {
        DeviceMeasurementDTO dto = gson.fromJson(new String(message.getBody()), DeviceMeasurementDTO.class);
        DeviceLastMeasurementDTO deviceMeasurementDTO = deviceLastMeasurementMapper.toDeviceLastMeasurementDTO(dto);
        Optional<DeviceLastMeasurementDTO> deviceLastMeasurementDTOOptional = deviceLastMeasurementService.getDeviceLastMeasurement(dto.getDeviceId());
        if (deviceLastMeasurementDTOOptional.isPresent()) {
            DeviceLastMeasurementDTO deviceLastMeasurementDTO = deviceLastMeasurementDTOOptional.get();
            Map<DeviceMeasurementOrder, Double> energyConsumptions = deviceMeasurementComputeService.computeMeasurement(
                    deviceLastMeasurementDTO,
                    deviceMeasurementDTO
            );
            if(energyConsumptions.size() > 1){
                deviceLastMeasurementService.updateDeviceLastMeasurement(
                        deviceMeasurementDTO.getUuid(),
                        deviceLastMeasurementMapper.toDeviceLastMeasurementUpdateDTO(deviceMeasurementDTO)
                );
                deviceHourlyMeasurementService.updateDeviceHourlyMeasurementValue(
                        deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementValueUpdateDTO(
                        deviceMeasurementDTO.getUuid(),
                        deviceMeasurementDTO.getLastDateTime(),
                        energyConsumptions.get(DeviceMeasurementOrder.OLD)
                    )
                );

                Optional<DeviceHourlyMeasurementDTO> deviceHourlyMeasurementDTOOptional =
                        deviceHourlyMeasurementService.getDeviceHourlyMeasurement(
                                deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementId(
                                        deviceLastMeasurementDTO.getUuid(),
                                        deviceLastMeasurementDTO.getLastDateTime()
                                )
                        );

                Optional<DeviceQueueDTO> deviceQueueDTOOptional = deviceQueueService.getDeviceQueue(deviceMeasurementDTO.getUuid());

                if(deviceHourlyMeasurementDTOOptional.isPresent() && deviceQueueDTOOptional.isPresent()){
                    DeviceHourlyMeasurementDTO deviceHourlyMeasurementDTO = deviceHourlyMeasurementDTOOptional.get();
                    DeviceQueueDTO deviceQueueDTO = deviceQueueDTOOptional.get();
                    if(deviceQueueDTO.getMaxWh() < deviceHourlyMeasurementDTO.getTotalEnergyConsumption()){
                        notificationService.sendNotification(deviceQueueDTO.getUserUuid(), deviceHourlyMeasurementDTO);
                    }
                }

                deviceMeasurementDTO.setLastMeasurement(energyConsumptions.get(DeviceMeasurementOrder.NEW));
                deviceHourlyMeasurementService.createDeviceHourlyMeasurement(
                        deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementCreateDTO(deviceMeasurementDTO)
                );
            }
            else {
                deviceLastMeasurementService.updateDeviceLastMeasurement(
                        deviceMeasurementDTO.getUuid(),
                        deviceLastMeasurementMapper.toDeviceLastMeasurementUpdateDTO(deviceMeasurementDTO)
                );
                deviceHourlyMeasurementService.updateDeviceHourlyMeasurementValue(
                        deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementValueUpdateDTO(
                                deviceMeasurementDTO.getUuid(),
                                deviceMeasurementDTO.getLastDateTime(),
                                energyConsumptions.get(DeviceMeasurementOrder.OLD)
                        )
                );
            }
        } else {
            deviceLastMeasurementService.createDeviceLastMeasurement(
                    deviceLastMeasurementMapper.toDeviceLastMeasurementCreateDTO(deviceMeasurementDTO)
            );
            deviceMeasurementDTO.setLastMeasurement(0.0);
            deviceHourlyMeasurementService.createDeviceHourlyMeasurement(
                    deviceHourlyMeasurementMapper.toDeviceHourlyMeasurementCreateDTO(deviceMeasurementDTO)
            );
        }
    }
}
