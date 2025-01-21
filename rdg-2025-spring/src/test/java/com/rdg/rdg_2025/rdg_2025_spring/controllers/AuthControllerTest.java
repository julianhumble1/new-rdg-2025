package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers=AuthController.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    AuthController authController;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtUtils jwtUtils;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    RoleRepository roleRepository;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Nested
    @DisplayName("auth controller sign in tests")
    class authControllerSignInTests {

        @Test
        void testRequestMissingPasswordResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(" \"username\": \"username\" "))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testRequestMissingUsernameResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(" \"password\": \"password\" "))
                    .andExpect(status().isBadRequest());

        }

    }

}
