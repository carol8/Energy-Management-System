package com.carol8.device_microservice.dto.device;

import com.carol8.device_microservice.enums.DeviceEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEventDTO {
    private DeviceQueueCreateDTO deviceQueueCreateDTO;
    private DeviceEvent deviceEvent;
}
