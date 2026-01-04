package com.prem.userservice.service;

import com.prem.userservice.dto.*;
import com.prem.userservice.exceptions.InvalidUsernameOrPassword;
import com.prem.userservice.exceptions.TokenExpiredException;
import com.prem.userservice.exceptions.UserAlreadyExistsException;
import com.prem.userservice.kafka.event.UserRegisteredEvent;
import com.prem.userservice.kafka.producer.UserEventProducer;
import com.prem.userservice.model.*;
import com.prem.userservice.repository.TokenRepository;
import com.prem.userservice.repository.UserRepository;
import com.prem.userservice.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserEventProducer userEventProducer;

    private UserServiceImpl userService;

    // Simulate @Value("${token.expiry}")
    private final int TOKEN_EXPIRY = 30;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, tokenRepository, passwordEncoder, userRoleRepository,
                userEventProducer);
        // We can't easily inject the private field via constructor, but we can rely on
        // default behavior or reflection if needed.
        // However, UserServiceImpl relies on @Value which isn't populated in pure unit
        // test.
        // We might need to use reflection to set 'tokenExpiry' or adjust the Service to
        // accept it in constructor.
        // For now, let's use Reflection to set the private field.
        try {
            java.lang.reflect.Field field = UserServiceImpl.class.getDeclaredField("tokenExpiry");
            field.setAccessible(true);
            field.setInt(userService, TOKEN_EXPIRY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set tokenExpiry", e);
        }
    }

    // --- SignUp Tests ---

    @Test
    void testSignUp_Success() {
        SignUpRequestDTO request = new SignUpRequestDTO();
        request.setEmail("test@example.com");
        request.setName("Test User");
        request.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Mocking save to return objects with ID
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        SignUpResponseDto response = userService.signUp(request);

        assertNotNull(response);
        assertEquals("Test User", response.getUser().getName());
        assertEquals("test@example.com", response.getUser().getEmail());

        verify(userRepository).save(any(User.class));
        verify(userEventProducer).publishUserRegisteredEvent(any(UserRegisteredEvent.class));
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void testSignUp_UserAlreadyExists() {
        SignUpRequestDTO request = new SignUpRequestDTO();
        request.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.signUp(request));

        verify(userRepository, never()).save(any(User.class));
        verify(userEventProducer, never()).publishUserRegisteredEvent(any(UserRegisteredEvent.class));
    }

    // --- Login Tests ---

    @Test
    void testLogin_Success() {
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setHashedPassword("encodedPassword");
        user.setStatus(Status.ACTIVE);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        LoginResponseDto response = userService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());

        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("unknown@example.com");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.login(request));
    }

    @Test
    void testLogin_InvalidPassword() {
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setHashedPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidUsernameOrPassword.class, () -> userService.login(request));
    }

    // --- Validate Tests ---

    @Test
    void testValidate_Success() {
        String tokenValue = "valid_token";
        Token token = new Token();
        token.setTokenValue(tokenValue);
        token.setExpiryDate(LocalDateTime.now().plusDays(1));
        token.setStatus(Status.ACTIVE);

        User user = new User();
        user.setEmail("user@example.com");
        token.setUser(user);

        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Optional.of(token));

        User result = userService.validate(tokenValue);

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void testValidate_TokenExpired() {
        String tokenValue = "expired_token";
        Token token = new Token();
        token.setTokenValue(tokenValue);
        token.setExpiryDate(LocalDateTime.now().minusDays(1)); // Expired
        token.setStatus(Status.ACTIVE);

        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Optional.of(token));

        assertThrows(TokenExpiredException.class, () -> userService.validate(tokenValue));

        // Ensure async deletion is triggered (might not be testable due to
        // CompletableFuture, but checking invocation is possible if synchronous)
        // Since it's runAsync, verify might be flaky without sleep or awaiting, but we
        // primarily check exception.
    }

    @Test
    void testGetUserById_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        // Note: UserMapper must be available. If it's MapStruct, ensure it works.
        // In unit test context, MapStruct instance (UserMapper.INSTANCE) should work if
        // generated classes are present.

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(userId.toString());

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }
}
