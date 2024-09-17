package com.prem.userservice.controller;

import com.prem.userservice.dto.*;
import com.prem.userservice.exceptions.UserAlreadyExistsException;
import com.prem.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDTO signupRequestDTO) throws UserAlreadyExistsException {
        return ResponseEntity.ok(userService.signUp(signupRequestDTO));
    }

    @PostMapping("/validate")
    public ResponseEntity<UserDto> validate(@RequestBody String token) {
        return ResponseEntity.ok(userService.validate(,token));
    }

    @PostMapping("/logout")
    public ResponseEntity<UserDto> validate(@RequestBody LogoutRequestDto token) {
        return ResponseEntity.ok(userService.logout(token));
    }
}
