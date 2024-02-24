package com.carol8.user_microservice.mapper;

import com.carol8.user_microservice.dto.*;
import com.carol8.user_microservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toUser(UserExtraCreateDTO userExtraCreateDTO){
        return User.builder()
                .username(userExtraCreateDTO.getUsername())
                .name(userExtraCreateDTO.getName())
                .build();
    }

    public UserExtraDTO toUserDTO(User user){
        return UserExtraDTO.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    public UserExtraDTOList toUserDTOList(List<User> userList){
        return UserExtraDTOList.builder()
                .userExtraDTOs(userList.stream()
                        .map(this::toUserDTO)
                        .toList())
                .build();
    }

    public UserExtraUuidDTO toUserUuidDTO(User user){
        return UserExtraUuidDTO.builder()
                .uuid(user.getUuid())
                .build();
    }

    public User updateUserFromDTO(User user, UserExtraUpdateDTO dto){
        if(!dto.getName().isEmpty()){
            user.setName(dto.getName());
        }
        return user;
    }
}
