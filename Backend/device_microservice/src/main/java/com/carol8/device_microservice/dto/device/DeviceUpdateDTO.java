package com.carol8.device_microservice.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUpdateDTO {
    private UUID userUuid;
    private String description;
    private String address;
    private double maxWh;
}
