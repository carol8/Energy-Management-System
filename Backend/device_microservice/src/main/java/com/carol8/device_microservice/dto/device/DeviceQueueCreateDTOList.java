package com.carol8.device_microservice.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceQueueCreateDTOList {
    List<DeviceQueueCreateDTO> deviceQueueCreateDTOS;
}
