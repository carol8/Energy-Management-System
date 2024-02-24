package com.carol8.security_microservice.controller;

import com.carol8.security_microservice.dto.auth.JwtDTO;
import com.carol8.security_microservice.dto.auth.UserLoginDTO;
import com.carol8.security_microservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@RequestBody UserLoginDTO dto) {
        Optional<JwtDTO> jwtDTOOptional = authService.login(dto);
        return jwtDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
