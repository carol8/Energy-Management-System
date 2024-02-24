package com.carol8.monitoring_microservice.dto.device;

import com.carol8.monitoring_microservice.enums.DeviceEvent;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEventDTO {
    private DeviceQueueCreateDTO deviceQueueCreateDTO;
    private DeviceEvent deviceEvent;
}
