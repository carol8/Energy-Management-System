package com.carol8.security_microservice.dto.user.auth;

import com.carol8.security_microservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDTO {
    private String username;
    private UserRole userRole;
}
