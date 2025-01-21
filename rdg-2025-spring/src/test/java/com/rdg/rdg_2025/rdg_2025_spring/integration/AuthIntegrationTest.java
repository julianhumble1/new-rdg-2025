package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import com.rdg.rdg_2025.rdg_2025_spring.utils.AuthTestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("auth sign in integration tests")
    class authSignInIntegrationTests {

        private static User testAdmin;
        private static User testUser;

        @BeforeAll
        public static void setUpExistingAdminAndUser(
                @Autowired UserRepository userRepository,
                @Autowired RoleRepository roleRepository,
                @Autowired PasswordEncoder passwordEncoder
        ) {

            userRepository.deleteAll();

            testAdmin = AuthTestUtils.createTestAdmin(userRepository, roleRepository, passwordEncoder);
            testUser = AuthTestUtils.createTestUser(userRepository, roleRepository, passwordEncoder);

        }

        @Test
        void testRequestMissingPasswordResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(" \"username\": \"test_user\" "))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testRequestBlankPasswordResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(" \"username\": \"test_user\", \"password\": \"\" "))
                    .andExpect(status().isBadRequest());

        }
    }


}
