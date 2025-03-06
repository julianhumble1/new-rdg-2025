package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.models.auth.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import com.rdg.rdg_2025.rdg_2025_spring.utils.AuthTestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    private static User testAdmin;
    private static String adminToken;

    private static User testUser;
    private static String userToken;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode requestJson;

    @BeforeAll
    public static void setupUsersAndTokens(@Autowired UserRepository userRepository,
                                           @Autowired RoleRepository roleRepository,
                                           @Autowired PasswordEncoder passwordEncoder,
                                           @Autowired AuthenticationManager authenticationManager,
                                           @Autowired JwtUtils jwtUtils) {

        userRepository.deleteAll();

        testAdmin = AuthTestUtils.createTestAdmin(userRepository, roleRepository, passwordEncoder);
        Authentication adminAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test_admin", "password123")
        );
        adminToken = "Bearer " + jwtUtils.generateJwtToken(adminAuthentication);

        testUser = AuthTestUtils.createTestUser(userRepository, roleRepository, passwordEncoder);
        Authentication userAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test_user", "password123")
        );
        userToken = "Bearer " + jwtUtils.generateJwtToken(userAuthentication);
    }

    @Nested
    @DisplayName("POST add new person integration tests")
    class AddNewPersonIntegrationTests {

        @BeforeEach
        void setup() {
            requestJson = objectMapper.createObjectNode();
            requestJson.put("firstName", "Test First Name");
            requestJson.put("lastName", "Test Last Name");
            requestJson.put("summary", "Test Summary");
            requestJson.put("homePhone", "01111 111111");
            requestJson.put("mobilePhone" ,"07111 111111");
            requestJson.put("addressStreet", "Test Street");
            requestJson.put("addressTown", "Test Town");
            requestJson.put("addressPostcode", "Test Postcode");
        }


        @Test
        void testSuccessfulAddWithAllFieldsResponds201() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());

        }

        @Test
        void testSuccessfulAddWithAllFieldsRespondsExpectedPersonJSON() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.person.firstName").value("Test First Name"))
                    .andExpect(jsonPath("$.person.lastName").value("Test Last Name"))
                    .andExpect(jsonPath("$.person.summary").value("Test Summary"))
                    .andExpect(jsonPath("$.person.homePhone").value("01111 111111"))
                    .andExpect(jsonPath("$.person.mobilePhone").value("07111 111111"))
                    .andExpect(jsonPath("$.person.addressStreet").value("Test Street"))
                    .andExpect(jsonPath("$.person.addressTown").value("Test Town"))
                    .andExpect(jsonPath("$.person.addressPostcode").value("Test Postcode"))
                    .andExpect(jsonPath("$.person.id").isNumber())
                    .andExpect(jsonPath("$.person.slug").value("test-first-name-test-last-name"))
                    .andExpect(jsonPath("$.person.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.person.updatedAt").isNotEmpty());


        }

        @Test
        void testSuccessfulAddAddsRowToDatabase() throws Exception {
            // Arrange
            int startingLength = personRepository.findAll().size();
            // Act
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)));
            // Assert
            int endingLength = personRepository.findAll().size();
            assertEquals(startingLength + 1, endingLength);
        }

        @Test
        void testDuplicateFirstNameAndLastNameResponds409() throws Exception {
            // Arrange
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());

            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isConflict());

        }

        @Test
        void testFirstNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("firstName", "");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }



    }

}
