package com.prem.userservice.dto;

import com.prem.userservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpResponseDto {
    private User user;
}
