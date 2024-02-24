package com.carol8.security_microservice.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private UUID uuid;
    private UUID userUuid;
    private String description;
    private String address;
    private double maxWh;
}
