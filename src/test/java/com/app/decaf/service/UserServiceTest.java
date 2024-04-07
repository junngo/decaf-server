package com.app.decaf.service;

import com.app.decaf.dto.UserRegistrationDto;
import com.app.decaf.model.User;
import com.app.decaf.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto registrationDto;
    private User user;

    @BeforeEach
    void setUp() {
        // Sample registration data
        registrationDto = new UserRegistrationDto("test@example.com", "password", "Test User");

        // Expected user object after mapping
        user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setName(registrationDto.getUsername());
        // Assume the password encoder returns "encodedPassword" for any input
        user.setPassword("encodedPassword");
    }

    @Test
    void whenRegisterUser_thenSaveUserWithEncodedPassword() {
        // Setup our mocked methods
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn(user.getPassword());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the method to test
        User savedUser = userService.registerUser(registrationDto);

        // Verify interactions
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(registrationDto.getPassword());

        // Assert the result
        assertEquals(registrationDto.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(registrationDto.getUsername(), savedUser.getName());
    }
}