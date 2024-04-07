package com.app.decaf.service;

import com.app.decaf.model.User;
import com.app.decaf.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void whenValidEmail_thenUserDetailsShouldBeReturned() {
        // Arrange
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encryptedPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails loadedUser = customUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertEquals(mockUser.getEmail(), loadedUser.getUsername());
        assertEquals(mockUser.getPassword(), loadedUser.getPassword());
        // Verify additional details as needed, such as authorities, if they're set or expected to be empty
    }

    @Test
    void whenInvalidEmail_thenUsernameNotFoundExceptionShouldBeThrown() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent@example.com");
        });
    }
}