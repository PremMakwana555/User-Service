package com.prem.userservice.service;

import com.prem.userservice.dto.*;
import com.prem.userservice.exceptions.InvalidUsernameOrPassword;
import com.prem.userservice.exceptions.TokenExpiredException;
import com.prem.userservice.exceptions.UserAlreadyExistsException;
import com.prem.userservice.model.Role;
import com.prem.userservice.model.Status;
import com.prem.userservice.model.Token;
import com.prem.userservice.model.User;
import com.prem.userservice.repository.TokenRepository;
import com.prem.userservice.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Value("${token.expiry}")
    private int tokenExpiry;

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
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getHashedPassword())) {
            throw new InvalidUsernameOrPassword("Invalid Username or Password");
        }

        Token token = generateToken(user);
        user.getTokens().add(token);

        tokenRepository.save(token);
        userRepository.save(user);
        return new LoginResponseDto(token.getTokenValue());
    }

    @Override
    public void logout(LogoutRequestDto logoutRequestDto) {

    }

    @Override
    public SignUpResponseDto signUp(SignUpRequestDTO signupRequestDTO) {
        userRepository.findByEmail(signupRequestDTO.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User already exists, Please login with your credentials");
                });

        User user = createUser(signupRequestDTO);
        userRepository.save(user);

        return new SignUpResponseDto(user);
    }

    @Override
    public User validate(String token){
        Token userToken = tokenRepository.findByTokenValue(token).get();
//        Token userToken = tokenRepository.findByTokenValue(token)
//                .filter(t -> !t.isExpired())
//                .orElseThrow(() -> new TokenExpiredException("Token is Invalid or Expired, Please login again"));
       return userToken.getUser();
    }

    /*
    *  Create a new user with the details from the request
    * */
    private User createUser(SignUpRequestDTO signupRequestDTO){
        User user = new User();
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setHashedPassword(bCryptPasswordEncoder.encode(signupRequestDTO.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRoles(List.of(Role.USER));
        return user;
    }

    // Generate a token for the user
    private Token generateToken(User user){
        Token token = new Token();
        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        token.setStatus(Status.ACTIVE);
        token.setExpiryDate(LocalDateTime.now().plusDays(tokenExpiry));
        return token;
    }

}
