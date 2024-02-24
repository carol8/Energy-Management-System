package com.carol8.security_microservice.mapper;

import com.carol8.security_microservice.dto.user.*;
import com.carol8.security_microservice.dto.user.auth.UserAuthDTO;
import com.carol8.security_microservice.dto.user.extra.UserExtraDTO;
import com.carol8.security_microservice.dto.user.extra.UserExtraDTOList;
import com.carol8.security_microservice.entity.User;
import com.carol8.security_microservice.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toUser(UserCreateDTO userCreateDTO){
        return User.builder()
                .username(userCreateDTO.getUsername())
                .password(passwordEncoder.encode(userCreateDTO.getPassword()))
                .role(userCreateDTO.getRole())
                .build();
    }

    public UserDTO toUserDTO(User user){
        return UserDTO.builder()
                .username(user.getUsername())
                .userRole(user.getRole())
                .build();
    }

    public UserDTOList toUserDTOList(List<User> userList){
        return UserDTOList.builder()
                .userDTOs(userList.stream()
                        .map(this::toUserDTO)
                        .toList())
                .build();
    }

    public UserRoleDTO toUserRoleDTO(UserRole userRole){
        return UserRoleDTO.builder()
                .userRole(userRole)
                .build();
    }

    public UserRoleDTOList toUserRoleDTOList(List<UserRole> userRoleList){
        return UserRoleDTOList.builder()
                .userRoleList(userRoleList.stream()
                        .map(this::toUserRoleDTO)
                        .toList())
                .build();
    }

    public Map<String,UserDTO> toUserDTOMap(List<User> userList) {
        return this.toUserDTOList(userList).getUserDTOs().stream()
                .collect(Collectors.toMap(UserDTO::getUsername, Function.identity()));
    }

    public UserDTO toCompleteUserDTO(UserAuthDTO userAuthDTO, UserExtraDTO userExtraDTO){
        return UserDTO.builder()
                .uuid(userExtraDTO.getUuid())
                .username(userAuthDTO.getUsername())
                .name(userExtraDTO.getName())
                .userRole(userAuthDTO.getUserRole())
                .build();
    }

    public UserDTOList toCompleteUserDTOList(Map<String, UserAuthDTO> authDTOMap, UserExtraDTOList extraDTOList){
        return UserDTOList.builder()
                .userDTOs(extraDTOList.getUserExtraDTOs().stream()
                        .map(userExtraDTO ->{
                            UserAuthDTO userAuthDTO = authDTOMap.get(userExtraDTO.getUsername());
                            return toCompleteUserDTO(userAuthDTO, userExtraDTO);
                        })
                        .toList())
                .build();
    }

    public User updateUserFromDTO(User user, UserUpdateDTO dto){
        if(!dto.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if(dto.getRole() != null){
            user.setRole(dto.getRole());
        }
        return user;
    }
}
