package com.carol8.security_microservice.mapper;

import com.carol8.security_microservice.dto.user.UserDTO;
import com.carol8.security_microservice.dto.user.UserDTOList;
import com.carol8.security_microservice.dto.user.auth.UserAuthDTO;
import com.carol8.security_microservice.dto.user.auth.UserAuthDTOList;
import com.carol8.security_microservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAuthMapper {
    public UserAuthDTO toUserAuthDTO(User user){
        return UserAuthDTO.builder()
                .username(user.getUsername())
                .userRole(user.getRole())
                .build();
    }

    public UserAuthDTOList toUserAuthDTOList(List<User> userList){
        return UserAuthDTOList.builder()
                .userAuthDTOs(userList.stream()
                        .map(this::toUserAuthDTO)
                        .toList())
                .build();
    }

    public Map<String, UserAuthDTO> toUserDTOMap(List<User> userList) {
        return this.toUserAuthDTOList(userList).getUserAuthDTOs().stream()
                .collect(Collectors.toMap(UserAuthDTO::getUsername, Function.identity()));
    }
}
