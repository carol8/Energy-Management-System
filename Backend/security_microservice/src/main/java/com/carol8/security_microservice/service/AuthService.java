package com.carol8.security_microservice.service;

import com.carol8.security_microservice.dto.auth.JwtDTO;
import com.carol8.security_microservice.dto.auth.UserLoginDTO;

import java.util.Optional;

public interface AuthService {
    Optional<JwtDTO> login(UserLoginDTO dto);
}
