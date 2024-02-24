package com.carol8.security_microservice.service.impl;

import com.carol8.security_microservice.dto.user.auth.UserAuthDTO;
import com.carol8.security_microservice.dto.user.extra.UserExtraDTO;
import com.carol8.security_microservice.dto.user.extra.UserExtraDTOList;
import com.carol8.security_microservice.mapper.UserAuthMapper;
import com.carol8.security_microservice.service.UserService;
import com.carol8.security_microservice.dto.user.*;
import com.carol8.security_microservice.entity.User;
import com.carol8.security_microservice.enums.UserRole;
import com.carol8.security_microservice.mapper.UserMapper;
import com.carol8.security_microservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserAuthMapper userAuthMapper;
    private final WebClient webClient;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserAuthMapper userAuthMapper, Environment env) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userAuthMapper = userAuthMapper;
        this.webClient = WebClient.create("http://" + env.getProperty("usermicroservice.ip") + ":" + env.getProperty("usermicroservice.port"));
    }

    @Override
    public ResponseEntity<UserDTOList> getUsers() throws ExecutionException, InterruptedException {
        Map<String, UserAuthDTO> userAuthDTOMap = userAuthMapper.toUserDTOMap(userRepository.findAll());
        ResponseEntity<UserExtraDTOList> userExtraDTOListResponseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .build())
                .retrieve()
                .toEntity(UserExtraDTOList.class)
                .toFuture()
                .get();
        return ResponseEntity
                .status(userExtraDTOListResponseEntity.getStatusCode())
                .body(userMapper.toCompleteUserDTOList(userAuthDTOMap, Objects.requireNonNull(userExtraDTOListResponseEntity.getBody())));
    }

    @Override
    public UserDTOList getUsersWithRole(UserRole userRole) {
        return userMapper.toUserDTOList(userRepository.findUsersByRoleIs(userRole));
    }

    @Override
    public ResponseEntity<UserDTO> getUser(String username) throws ExecutionException, InterruptedException {
        Optional<User> userOptional = userRepository.findById(username);

        if(userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UserAuthDTO userAuthDTO = userAuthMapper.toUserAuthDTO(userOptional.get());
        ResponseEntity<UserExtraDTO> userExtraDTOResponseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/" + username)
                        .build())
                .retrieve()
                .toEntity(UserExtraDTO.class)
                .toFuture()
                .get();

        return ResponseEntity
                .status(userExtraDTOResponseEntity.getStatusCode())
                .body(userMapper.toCompleteUserDTO(userAuthDTO, Objects.requireNonNull(userExtraDTOResponseEntity.getBody())));
    }

    @Override
    public UserRoleDTOList getUserRoles() {
        return userMapper.toUserRoleDTOList(List.of(UserRole.values()));
    }

    @Override
    @Transactional(rollbackOn = {ExecutionException.class, InterruptedException.class})
    public ResponseEntity<UserDTO> createUser(UserCreateDTO dto) throws ExecutionException, InterruptedException {
        if (userRepository.existsById(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UserAuthDTO userAuthDTO = userAuthMapper.toUserAuthDTO(userRepository.save(userMapper.toUser(dto)));

        ResponseEntity<UserExtraDTO> userExtraDTOResponseEntity = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .retrieve()
                .toEntity(UserExtraDTO.class)
                .toFuture()
                .get();

        return ResponseEntity
                .status(userExtraDTOResponseEntity.getStatusCode())
                .body(userMapper.toCompleteUserDTO(userAuthDTO, Objects.requireNonNull(userExtraDTOResponseEntity.getBody())));
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(String username, UserUpdateDTO dto) throws ExecutionException, InterruptedException {
        Optional<User> userOptional = userRepository.findById(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User updatedUser = userMapper.updateUserFromDTO(userOptional.get(), dto);
        userRepository.save(updatedUser);

        ResponseEntity<UserExtraDTO> userExtraDTOResponseEntity = webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/" + username)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .retrieve()
                .toEntity(UserExtraDTO.class)
                .toFuture()
                .get();

        return ResponseEntity
                .status(userExtraDTOResponseEntity.getStatusCode())
                .body(userMapper.toCompleteUserDTO(userAuthMapper.toUserAuthDTO(updatedUser), Objects.requireNonNull(userExtraDTOResponseEntity.getBody())));
    }

    @Override
    public ResponseEntity<UserDTO> deleteUser(String username) throws ExecutionException, InterruptedException {
        Optional<User> userOptional = userRepository.findById(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ResponseEntity<UserExtraDTO> userExtraDTOResponseEntity = webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/" + username)
                        .build())
                .retrieve()
                .toEntity(UserExtraDTO.class)
                .toFuture()
                .get();

        User user = userOptional.get();
        userRepository.delete(user);

        return ResponseEntity
                .status(userExtraDTOResponseEntity.getStatusCode())
                .body(userMapper.toCompleteUserDTO(userAuthMapper.toUserAuthDTO(user), Objects.requireNonNull(userExtraDTOResponseEntity.getBody())));

    }
}