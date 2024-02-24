package com.carol8.user_microservice.service.impl;

import com.carol8.user_microservice.dto.*;
import com.carol8.user_microservice.entity.User;
import com.carol8.user_microservice.mapper.UserMapper;
import com.carol8.user_microservice.repository.UserRepository;
import com.carol8.user_microservice.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final WebClient webClient;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, Environment env) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.webClient = WebClient.create("http://" + env.getProperty("devicemicroservice.ip") + ":" + env.getProperty("devicemicroservice.port"));
    }

    @Override
    public UserExtraDTOList getUsers() {
        return userMapper.toUserDTOList(userRepository.findAll());
    }

    @Override
    public Optional<UserExtraDTO> getUser(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserDTO);
    }

    @Override
    @Transactional(rollbackOn = {ExecutionException.class, InterruptedException.class})
    public Optional<UserExtraDTO> createUser(UserExtraCreateDTO dto) throws ExecutionException, InterruptedException {
        if (!userRepository.existsByUsername(dto.getUsername())) {
            User user = userRepository.save(userMapper.toUser(dto));
            webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/users")
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(userMapper.toUserUuidDTO(user)))
                    .retrieve()
                    .toEntity(Object.class)
                    .toFuture()
                    .get();
            return Optional.of(userMapper.toUserDTO(user));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserExtraDTO> updateUser(String username, UserExtraUpdateDTO dto) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User updatedUser = userMapper.updateUserFromDTO(userOptional.get(), dto);
            userRepository.save(updatedUser);
            return Optional.of(userMapper.toUserDTO(updatedUser));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserExtraDTO> deleteUser(String username) throws ExecutionException, InterruptedException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            webClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/devices")
                            .queryParam("userUuid", user.getUuid())
                            .build())
                    .retrieve()
                    .toEntity(Object.class)
                    .toFuture()
                    .get();
            userRepository.delete(user);
            return Optional.of(userMapper.toUserDTO(user));
        }
        return Optional.empty();
    }
}