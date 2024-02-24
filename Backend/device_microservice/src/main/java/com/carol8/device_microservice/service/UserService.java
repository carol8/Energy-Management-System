package com.carol8.device_microservice.service;

import com.carol8.device_microservice.dto.user.UserCreateDTO;
import com.carol8.device_microservice.dto.user.UserDTO;
import com.carol8.device_microservice.dto.user.UserDTOList;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserDTOList getUsers();
    Optional<UserDTO> createUser(UserCreateDTO dto);
    Optional<UserDTO> deleteUser(UUID uuid);
}
