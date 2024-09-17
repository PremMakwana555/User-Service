package com.prem.userservice.dto;

import com.prem.userservice.model.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
}
