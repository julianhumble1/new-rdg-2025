package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.auth.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.*;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CreditIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreditRepository creditRepository;

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

    @AfterAll
    public static void destroyUsers(@Autowired UserRepository userRepository, @Autowired VenueRepository venueRepository) {
        userRepository.deleteAll();
        venueRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST add new credit integration tests")
    class AddNewCreditIntegrationTests {

        private Production testProduction;
        private int testProductionId;

        private Person testPerson;
        private int testPersonId;

        @Autowired
        private ProductionRepository productionRepository;

        @Autowired
        private PersonRepository personRepository;

        @BeforeEach
        void setup() {
            testProduction = new Production(
                    "Test Production",
                    null,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test File String"
            );
            productionRepository.save(testProduction);
            testProductionId = testProduction.getId();

            testPerson = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            personRepository.save(testPerson);
            testPersonId = testPerson.getId();

            requestJson = objectMapper.createObjectNode();
            requestJson.put("name", "Test Credit");
            requestJson.put("type", "ACTOR");
            requestJson.put("productionId", testProductionId);
            requestJson.put("personId", testPersonId);
            requestJson.put("summary", "Test Summary");
        }

        @Test
        void testSuccessfulAddResponds201() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSuccessfulAddRespondsExpectedJSON() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.credit.name").value("Test Credit"))
                    .andExpect(jsonPath("$.credit.type").value("ACTOR"))
                    .andExpect(jsonPath("$.credit.summary").value("Test Summary"))
                    .andExpect(jsonPath("$.credit.person.firstName").value("Test First Name"))
                    .andExpect(jsonPath("$.credit.production.name").value("Test Production"))
                    .andExpect(jsonPath("$.credit.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.credit.updatedAt").isNotEmpty());
        }

        @Test
        void testSuccessfulAddIncreasesNumberOfCredits() throws Exception {
            // Arrange
            int originalLength = creditRepository.findAll().size();
            // Act
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
            // Assert
            int finalLength = creditRepository.findAll().size();
            assertEquals(originalLength + 1, finalLength);
        }

        @Test
        void testNonExistentProductionIdResponds404() throws Exception {
            // Arrange
            requestJson.put("productionId", testProductionId + 1);
            assertFalse(productionRepository.existsById(testProductionId + 1));
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentPersonIdResponds404() throws Exception {
            // Arrange
            requestJson.put("personId", testPersonId + 1);
            assertFalse(personRepository.existsById(testPersonId + 1));
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("name", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("name");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("type", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeNotAppropriateResponds400() throws Exception {
            // Arrange
            requestJson.put("type", "not actor musician or producer");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("productionId");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdNotIntResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", "not an int");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdZeroResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", 0);
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testPersonIdNotIntResponds400() throws Exception {
            // Arrange
            requestJson.put("personId", "not an int");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testSummaryAndPersonIdEmptyResponds201() throws Exception {
            // Arrange
            requestJson.put("summary", "");
            requestJson.put("personId", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.credit.name").value("Test Credit"))
                    .andExpect(jsonPath("$.credit.type").value("ACTOR"))
                    .andExpect(jsonPath("$.credit.summary").value(""))
                    .andExpect(jsonPath("$.credit.person").isEmpty())
                    .andExpect(jsonPath("$.credit.production.name").value("Test Production"))
                    .andExpect(jsonPath("$.credit.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.credit.updatedAt").isNotEmpty());
        }

        @Test
        void testTypeMusicianResponds201() throws Exception {
            // Arrange
            requestJson.put("type", "MUSICIAN");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.credit.type").value("MUSICIAN"));
        }

        @Test
        void testTypeProducerResponds201() throws Exception {
            // Arrange
            requestJson.put("type", "PRODUCER");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.credit.type").value("PRODUCER"));
        }

        @Test
        void testPersonIdZeroResponds201() throws Exception {
            // Arrange
            requestJson.put("personId", 0);
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.credit.person").isEmpty());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isForbidden());
        }


    }
}
