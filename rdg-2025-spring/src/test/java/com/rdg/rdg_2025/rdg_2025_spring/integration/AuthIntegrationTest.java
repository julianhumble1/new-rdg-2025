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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Nested
    @DisplayName("auth sign in integration tests")
    class authSignInIntegrationTests {

        @Test
        void testRequestMissingPasswordResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_user\" }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testRequestBlankPasswordResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_user\", \"password\": \"\" }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testRequestMissingUsernameResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{  \"password\": \"password123\" }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testRequestBlankUsernameResponds400() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"\",  \"password\": \"password123\" }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testCorrectAdminUsernameAndPasswordResponds200() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_admin\",  \"password\": \"password123\" }"))
                    .andExpect(status().isOk());

        }

        @Test
        void testCorrectAdminUsernameAndPasswordRespondsWithJwtToken() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_admin\",  \"password\": \"password123\" }"))
                    .andExpect(jsonPath("$.token").isNotEmpty());

        }

        @Test
        void testCorrectAdminUsernameAndPasswordRespondsWithExpectedDetails() throws Exception {
            // Arrange
            Long adminId = testAdmin.getId();
            String username = testAdmin.getUsername();
            String email = testAdmin.getEmail();

            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_admin\",  \"password\": \"password123\" }"))
                    .andExpect(jsonPath("$.id").value(adminId))
                    .andExpect(jsonPath("$.username").value(username))
                    .andExpect(jsonPath("$.email").value(email))
                    .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"));

        }

        @Test
        void testCorrectUserUsernameAndPasswordResponds200() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_user\",  \"password\": \"password123\" }"))
                    .andExpect(status().isOk());

        }

        @Test
        void testCorrectUserUsernameAndPasswordRespondsWithJwtToken() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_user\",  \"password\": \"password123\" }"))
                    .andExpect(jsonPath("$.token").isNotEmpty());

        }

        @Test
        void testCorrectUserUsernameAndPasswordRespondsWithExpectedDetails() throws Exception {
            // Arrange
            Long userId = testUser.getId();
            String username = testUser.getUsername();
            String email = testUser.getEmail();

            // Act
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_user\",  \"password\": \"password123\" }"))
                    .andExpect(jsonPath("$.id").value(userId))
                    .andExpect(jsonPath("$.username").value(username))
                    .andExpect(jsonPath("$.email").value(email))
                    .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        }

        @Test
        void testUsernameNotInDatabaseResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"username\": \"non_existent_user\",  \"password\": \"password123\" }"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testPasswordDoesNotMatchUsernameThrows401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"test_user\",  \"password\": \"not_the_password\" }"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("sign up integration tests")
    class signUpIntegrationTests {

        @Test
        void testSignUpUserWithFullDetailsResponds200() throws Exception {
            mockMvc.perform(post("/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\": \"new_user\", \"email\": \"user@new.com\", \"password\": \"password123\", \"role\": [\"user\"] }"))
                    .andExpect(status().isOk());
        }

        @Test
        void testSignUpAdminWithFullDetailsResponds200() throws Exception {
            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\": \"new_admin\", \"email\": \"admin@new.com\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isOk());
        }

        @Test
        void testMissingUsernameResponds400() throws Exception {
            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"email\": \"admin@new.com\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testBlankUsernameResponds400() throws Exception {

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"\", \"email\": \"admin@new.com\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testShortUsernameResponds400() throws Exception {

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"ab\", \"email\": \"admin@new.com\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testLongUsernameResponds400() throws Exception {

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"abcdefghijklmnopqrstuvwxyz\", \"email\": \"admin@new.com\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testEmailBlankResponds400() throws Exception {

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\": \"new_admin\", \"email\": \"\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testEmailMissingResponds400() throws Exception {

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\": \"new_admin\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testBadEmailResponds400() throws Exception {

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\": \"new_admin\", \"email\": \"not an email\", \"password\": \"password123\", \"role\": [\"admin\"] }"))
                    .andExpect(status().isBadRequest());

        }

    }

}
