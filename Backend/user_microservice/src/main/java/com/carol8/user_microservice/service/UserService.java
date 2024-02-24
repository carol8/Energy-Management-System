package com.carol8.user_microservice.service;

import com.carol8.user_microservice.dto.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserService {
    UserExtraDTOList getUsers();
    Optional<UserExtraDTO> getUser(String username);
    Optional<UserExtraDTO> createUser(UserExtraCreateDTO dto) throws ExecutionException, InterruptedException;
    Optional<UserExtraDTO> updateUser(String username, UserExtraUpdateDTO dto);
    Optional<UserExtraDTO> deleteUser(String username) throws ExecutionException, InterruptedException;
}
