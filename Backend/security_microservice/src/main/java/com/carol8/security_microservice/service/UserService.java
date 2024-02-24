package com.carol8.security_microservice.service;

import com.carol8.security_microservice.dto.user.*;
import com.carol8.security_microservice.enums.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

public interface UserService {
    ResponseEntity<UserDTOList> getUsers() throws ExecutionException, InterruptedException;
    UserDTOList getUsersWithRole(UserRole userRole);
    ResponseEntity<UserDTO> getUser(String username) throws ExecutionException, InterruptedException;
    UserRoleDTOList getUserRoles();
    ResponseEntity<UserDTO> createUser(UserCreateDTO dto) throws ExecutionException, InterruptedException;
    ResponseEntity<UserDTO> updateUser(String username, UserUpdateDTO dto) throws ExecutionException, InterruptedException;
    ResponseEntity<UserDTO> deleteUser(String username) throws ExecutionException, InterruptedException;
}
