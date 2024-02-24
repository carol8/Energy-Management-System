package com.carol8.security_microservice.service.impl;

import com.carol8.security_microservice.dto.auth.JwtDTO;
import com.carol8.security_microservice.dto.auth.UserLoginDTO;
import com.carol8.security_microservice.entity.User;
import com.carol8.security_microservice.repository.UserRepository;
import com.carol8.security_microservice.service.AuthService;
import com.carol8.security_microservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Optional<JwtDTO> login(UserLoginDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        Optional<User> user = userRepository.findById(dto.getUsername());
        return user.map(value -> JwtDTO.builder().token(jwtService.generateToken(value)).build());
    }
}
