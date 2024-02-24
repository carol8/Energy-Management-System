package com.carol8.user_microservice.controller;

import com.carol8.user_microservice.dto.*;
import com.carol8.user_microservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    ResponseEntity<UserExtraDTOList> getUsers() {
        UserExtraDTOList userListDTO = userService.getUsers();
        return ResponseEntity.ok(userListDTO);
    }

    @GetMapping("/{username}")
    ResponseEntity<UserExtraDTO> getUser(@PathVariable String username) {
        Optional<UserExtraDTO> userDTOOptional = userService.getUser(username);
        return userDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping
    ResponseEntity<UserExtraDTO> createUser(@RequestBody UserExtraCreateDTO dto) {
        try {
            Optional<UserExtraDTO> userDTOOptional = userService.createUser(dto);
            return userDTOOptional
                    .map(userDTO -> ResponseEntity.status(HttpStatus.CREATED).body(userDTO))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PatchMapping("/{username}")
    ResponseEntity<UserExtraDTO> updateUser(@PathVariable String username, @RequestBody UserExtraUpdateDTO dto) {
        Optional<UserExtraDTO> userDTOOptional = userService.updateUser(username, dto);
        return userDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/{username}")
    ResponseEntity<UserExtraDTO> deleteUser(@PathVariable String username) {
        try {
            Optional<UserExtraDTO> userDTOOptional = userService.deleteUser(username);
            return userDTOOptional
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
