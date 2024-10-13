package com.prem.userservice.controller;

import com.prem.userservice.dto.*;
import com.prem.userservice.mappings.UserMapper;
import com.prem.userservice.model.User;
import com.prem.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(userService.signUp(signupRequestDTO));
    }

    @GetMapping("/validate")
    public ResponseEntity<UserDto> validate(@RequestHeader("Authorization") String token) {
        User user = userService.validate(token);
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDto token) {
        userService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
