package com.prem.userservice.service;

import com.prem.userservice.dto.*;
import com.prem.userservice.exceptions.UserAlreadyExistsException;
import com.prem.userservice.model.Role;
import com.prem.userservice.model.Status;
import com.prem.userservice.model.User;
import com.prem.userservice.repository.TokenRepository;
import com.prem.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository
//                           BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        return null;
    }

    @Override
    public void logout(LogoutRequestDto logoutRequestDto) {

    }

    @Override
    public SignUpResponseDto signUp(SignUpRequestDTO signupRequestDTO) throws UserAlreadyExistsException {
        Optional<User> existingUser = userRepository.findByEmail(signupRequestDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setHashedPassword(signupRequestDTO.getPassword());
        user.setStatus(Status.ACTIVE);
        user.setRoles(List.of(Role.USER));
        userRepository.save(user);

        return new SignUpResponseDto(user);
    }

    @Override
    public User validate(UserDto userDto, String token) {
        return null;
    }
}
