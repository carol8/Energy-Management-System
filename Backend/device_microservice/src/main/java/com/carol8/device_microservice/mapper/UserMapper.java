package com.carol8.device_microservice.mapper;

import com.carol8.device_microservice.dto.user.UserCreateDTO;
import com.carol8.device_microservice.dto.user.UserDTO;
import com.carol8.device_microservice.dto.user.UserDTOList;
import com.carol8.device_microservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public User toUser(UserCreateDTO userCreateDTO){
        return User.builder()
                .uuid(userCreateDTO.getUuid())
                .build();
    }

    public UserDTO toUserDTO(User user){
        return UserDTO.builder()
                .uuid(user.getUuid())
                .build();
    }

    public UserDTOList toUserDTOList(List<User> userList){
        return UserDTOList.builder()
                .userDTOs(userList.stream()
                        .map(this::toUserDTO)
                        .toList())
                .build();
    }
}
