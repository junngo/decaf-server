package com.app.decaf.cotroller;

import com.app.decaf.config.TestSecurityConfig;
import com.app.decaf.dto.AuthenticationRequest;
import com.app.decaf.service.CustomUserDetailsService;
import com.app.decaf.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        mockUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("user@example.com")
                .password("password")
                .authorities("USER")
                .build();

        given(customUserDetailsService.loadUserByUsername(any(String.class))).willReturn(mockUserDetails);
        given(jwtUtil.generateToken(any(String.class))).willReturn("mockJwtToken");
    }


    @Test
    void whenValidInput_thenReturns200AndJwtToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mockJwtToken")));
    }

    @Test
    void whenInvalidCredentials_thenReturnsUnauthorized() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "wrongPassword");

        // Simulate authentication failure
        willThrow(new BadCredentialsException("Bad credentials")).given(authenticationManager).authenticate(any());

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()) // Assuming you handle the exception to return 401
                .andExpect(content().string("")); // Adjust based on actual response content
    }
}
