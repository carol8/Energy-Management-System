package com.carol8.security_microservice.dto.user.extra;

import com.carol8.security_microservice.dto.user.auth.UserAuthDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExtraDTOList {
    private List<UserExtraDTO> userExtraDTOs;
}
