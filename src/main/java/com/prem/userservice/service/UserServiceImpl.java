package com.prem.userservice.service;

import com.prem.userservice.dto.*;
import com.prem.userservice.exceptions.InvalidUsernameOrPassword;
import com.prem.userservice.exceptions.UserAlreadyExistsException;
import com.prem.userservice.model.Role;
import com.prem.userservice.model.Status;
import com.prem.userservice.model.Token;
import com.prem.userservice.model.User;
import com.prem.userservice.repository.TokenRepository;
import com.prem.userservice.repository.UserRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.System.currentTimeMillis;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<User> user= userRepository.findByEmail(loginRequestDto.getEmail());

        if (user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.get().getHashedPassword())){
            throw new InvalidUsernameOrPassword("User not found");
        }

        Token token = new Token();
        token.setToken(generateToken());
        token.setUser(user.get());
        token.setStatus(Status.ACTIVE);
        token.setExpired(false);
        token.setExpiryDate(new Date(currentTimeMillis() + 86400000));
        user.get().getTokens().add(token);
        userRepository.save(user.get());
        tokenRepository.save(token);
        return new LoginResponseDto(token.getToken());
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
        user.setHashedPassword(bCryptPasswordEncoder.encode(signupRequestDTO.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRoles(List.of(Role.USER));
        userRepository.save(user);

        return new SignUpResponseDto(user);
    }

    @Override
    public User validate(UserDto userDto, String token) {
        return null;
    }

    private String generateToken(){
        return RandomStringUtils.randomAlphanumeric(128);
    }
}
