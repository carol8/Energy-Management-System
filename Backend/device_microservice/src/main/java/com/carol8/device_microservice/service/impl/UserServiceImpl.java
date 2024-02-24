package com.carol8.device_microservice.service.impl;

import com.carol8.device_microservice.dto.user.UserCreateDTO;
import com.carol8.device_microservice.dto.user.UserDTO;
import com.carol8.device_microservice.dto.user.UserDTOList;
import com.carol8.device_microservice.entity.User;
import com.carol8.device_microservice.mapper.UserMapper;
import com.carol8.device_microservice.repository.UserRepository;
import com.carol8.device_microservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTOList getUsers() {
        return userMapper.toUserDTOList(userRepository.findAll());
    }

    @Override
    public Optional<UserDTO> createUser(UserCreateDTO dto) {
        if (!userRepository.existsById(dto.getUuid())) {
            User user = userRepository.save(userMapper.toUser(dto));
            return Optional.of(userMapper.toUserDTO(user));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDTO> deleteUser(UUID uuid) {
        Optional<User> userOptional = userRepository.findById(uuid);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
            return Optional.of(userMapper.toUserDTO(user));
        }
        return Optional.empty();
    }
}
