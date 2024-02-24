package com.carol8.device_microservice.controller;

import com.carol8.device_microservice.dto.user.UserCreateDTO;
import com.carol8.device_microservice.dto.user.UserDTO;
import com.carol8.device_microservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO dto) {
        Optional<UserDTO> userDTOOptional = userService.createUser(dto);
        return userDTOOptional
                .map(userDTO -> ResponseEntity.status(HttpStatus.CREATED).body(userDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/{uuid}")
    ResponseEntity<UserDTO> deleteUser(@PathVariable UUID uuid) {
            Optional<UserDTO> userDTOOptional = userService.deleteUser(uuid);
            return userDTOOptional
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
