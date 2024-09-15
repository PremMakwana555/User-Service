package com.prem.userservice.service;

import com.prem.userservice.dto.*;
import com.prem.userservice.exceptions.UserAlreadyExistsException;
import com.prem.userservice.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    void logout(LogoutRequestDto logoutRequestDto);
    SignUpResponseDto signUp(SignUpRequestDTO signupRequestDTO) throws UserAlreadyExistsException;
    User validate(UserDto userDto, String token);
}
