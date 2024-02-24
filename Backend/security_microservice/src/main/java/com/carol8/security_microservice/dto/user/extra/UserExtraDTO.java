package com.carol8.security_microservice.dto.user.extra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExtraDTO {
    private UUID uuid;
    private String username;
    private String name;
}
