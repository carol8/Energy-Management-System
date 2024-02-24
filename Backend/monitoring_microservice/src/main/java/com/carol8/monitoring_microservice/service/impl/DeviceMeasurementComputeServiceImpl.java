package com.carol8.monitoring_microservice.service.impl;

import com.carol8.monitoring_microservice.dto.device.DeviceLastMeasurementDTO;
import com.carol8.monitoring_microservice.enums.DeviceMeasurementOrder;
import com.carol8.monitoring_microservice.service.DeviceMeasurementComputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

import static com.carol8.monitoring_microservice.enums.DeviceMeasurementOrder.OLD;
import static com.carol8.monitoring_microservice.enums.DeviceMeasurementOrder.NEW;

@Service
@RequiredArgsConstructor
public class DeviceMeasurementComputeServiceImpl implements DeviceMeasurementComputeService {
    @Override
    public Map<DeviceMeasurementOrder, Double> computeMeasurement(DeviceLastMeasurementDTO lastMeasurementDTO, DeviceLastMeasurementDTO measurementDTO) {
        if(lastMeasurementDTO.getLastDateTime().getHour() != measurementDTO.getLastDateTime().getHour()){
            Duration durationOld = Duration.between(
                    lastMeasurementDTO.getLastDateTime(),
                    measurementDTO.getLastDateTime().withMinute(0).withSecond(0).withNano(0)
            );
            Duration durationNew = Duration.between(
                    measurementDTO.getLastDateTime().withMinute(0).withSecond(0).withNano(0),
                    measurementDTO.getLastDateTime()
            );
            double interpolationValue = ((double) durationOld.toMillis()) / (durationOld.toMillis() + durationNew.toMillis());
            double interpolatedEnergyConsumption = measurementDTO.getLastMeasurement() * interpolationValue +
                    lastMeasurementDTO.getLastMeasurement() * (1 - interpolationValue);
            double energyConsumptionOld = (durationOld.toMillis() / 3600000.0) *
                    (lastMeasurementDTO.getLastMeasurement() + interpolatedEnergyConsumption) / 2.0;
            double energyConsumptionNew = (durationOld.toMillis() / 3600000.0) *
                    (interpolatedEnergyConsumption + measurementDTO.getLastMeasurement()) / 2.0;

            return Map.of(OLD, energyConsumptionOld, NEW, energyConsumptionNew);
        }
        else {
            Duration duration = Duration.between(lastMeasurementDTO.getLastDateTime(), measurementDTO.getLastDateTime());
            Double energyConsumption = (duration.toMillis() / 3600000.0) *
                    (measurementDTO.getLastMeasurement() + lastMeasurementDTO.getLastMeasurement()) / 2.0;
            return Map.of(OLD, energyConsumption);
        }
    }
}
