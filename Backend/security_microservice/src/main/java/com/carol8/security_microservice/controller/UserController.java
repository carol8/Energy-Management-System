package com.carol8.security_microservice.controller;

import com.carol8.security_microservice.dto.user.*;
import com.carol8.security_microservice.enums.UserRole;
import com.carol8.security_microservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("CallToPrintStackTrace")
@CrossOrigin
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    ResponseEntity<?> getUsers() {
        try {
            return userService.getUsers();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }

    @GetMapping("/role")
    ResponseEntity<?> getUsersWithRole(@RequestParam UserRole userRole) {
        return ResponseEntity.ok(userService.getUsersWithRole(userRole));
    }

    @GetMapping("/{username}")
    ResponseEntity<?> getUser(@PathVariable String username) {
        try {
            return userService.getUser(username);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }

    @GetMapping("/roles")
    ResponseEntity<UserRoleDTOList> getUserRoles(){
        UserRoleDTOList userRoleDTOList = userService.getUserRoles();
        return ResponseEntity.ok(userRoleDTOList);
    }

    @PostMapping
    ResponseEntity<?> createUser(@RequestBody UserCreateDTO dto) {
        try {
            return userService.createUser(dto);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }

    @PatchMapping("/{username}")
    ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserUpdateDTO dto) {
        try {
            return userService.updateUser(username, dto);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }

    @DeleteMapping("/{username}")
    ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            return userService.deleteUser(username);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e);
        }
    }
}
