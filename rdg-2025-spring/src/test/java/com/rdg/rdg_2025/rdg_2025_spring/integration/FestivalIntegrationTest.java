package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.User;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class FestivalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FestivalRepository festivalRepository;

    private static User testAdmin;
    private static String adminToken;

    private static User testUser;
    private static String userToken;

    private static Venue testVenue1;
    private static Venue testVenue2;

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

    @BeforeAll
    public static void addVenuesToDatabase(@Autowired VenueRepository venueRepository) {

        venueRepository.deleteAll();

        testVenue1 = new Venue("Test Venue 1", null, null, null, null, null);
        testVenue2 = new Venue("Test Venue 2", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

        venueRepository.save(testVenue1);
        venueRepository.save(testVenue2);
    }

    @AfterAll
    public static void destroyUsers(@Autowired UserRepository userRepository, @Autowired VenueRepository venueRepository) {
        userRepository.deleteAll();
        venueRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {festivalRepository.deleteAll();}

    @AfterEach
    public void cleanup() {festivalRepository.deleteAll();}

    @Nested
    @DisplayName("addNewFestival integration tests")
    class addNewFestivalIntegrationTests {

        @Test
        void testFullProductionDetailsWithNoVenueReturns201() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{\"name\": \"Test Festival\", \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testFullFestivalDetailsWithNoVenueReturnsExpectedProductionObject() throws Exception {
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{\"name\": \"Test Festival\", \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(jsonPath("$.festival.name").value("Test Festival")
                    );
        }

        @Test
        void testFullProductionDetailsWithValidVenueReturns201() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{\"name\": \"Test Festival\",\"venueId\": " + testVenueId + ", \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testOnlyNameAndYearReturns201() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{\"name\": \"Test Festival\", \"year\": 2025}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testInvalidVenueIdResponds400BadRequest() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{\"name\": \"Test Festival\",\"venueId\": " + (testVenueId - 1) + ", \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testVenueIdNotIntResponds400BadRequest() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{\"name\": \"Test Festival\",\"venueId\": \"Bad Venue Id\", \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(status().isBadRequest());
        }



    }
}
