package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.*;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PerformanceIntegrationTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private MockMvc mockMvc;

    private static User testAdmin;
    private static String adminToken;

    private static User testUser;
    private static String userToken;

    private Venue testVenue;
    int testVenueId;

    private Production testProduction;
    int testProductionId;

    private Festival testFestival;
    int testFestivalId;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

    @BeforeEach
    public void populateDatabase(@Autowired VenueRepository venueRepository,
                                 @Autowired ProductionRepository productionRepository,
                                 @Autowired FestivalRepository festivalRepository) {

        testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

        testProduction = new Production(
                "Test Production",
                testVenue,
                "Test Author",
                "Test Description",
                LocalDateTime.parse("2025-10-10T10:00:00", formatter),
                false,
                false,
                "Test File String"
        );

        testFestival = new Festival("Test Festival", testVenue, 2025, 1, "Test Description");

        venueRepository.save(testVenue);
        testVenueId = testVenue.getId();
        productionRepository.save(testProduction);
        testProductionId = testProduction.getId();
        festivalRepository.save(testFestival);
        testFestivalId = testFestival.getId();

    }

    @Nested
    @DisplayName("POST add new performance integration tests")
    class PostAddNewPerformanceIntegrationTests {

        @Test
        void testSuccessfulAddWithAllFieldsResponds201() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProductionId +  ", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"festivalId\": " + testFestivalId + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSuccessfulAddWithOnlyMandatoryFieldsResponds201() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProductionId +  ", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\" " +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testPerformanceInDatabaseFollowingSuccessfulAdd() throws Exception {
            // Arrange
            List<Performance> performanceList = performanceRepository.findAll();
            int startingLength = performanceList.size();

            // Act
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProductionId +  ", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"festivalId\": " + testFestivalId + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ));
            // Assert
            performanceList = performanceRepository.findAll();
            assertEquals(startingLength + 1, performanceList.size());
        }

        @Test
        void testFullDetailsSuccessfulAddRespondsExpectedPerformanceObject() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/performances")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(
                            "{" +
                                    "\"productionId\": " + testProductionId +  ", " +
                                    "\"venueId\": " + testVenueId + ", " +
                                    "\"festivalId\": " + testFestivalId + ", " +
                                    "\"time\": \"2025-10-10T10:00:00\", " +
                                    "\"description\": \"Test Performance Description\", " +
                                    "\"standardPrice\": \"10.00\", " +
                                    "\"concessionPrice\": \"9.00\", " +
                                    "\"boxOffice\": \"Test Box Office\" " +
                                    "}"
                    ))
                    .andExpect(jsonPath("$.performance.production.id").value(testProductionId))
                    .andExpect(jsonPath("$.performance.venue.id").value(testVenueId))
                    .andExpect(jsonPath("$.performance.festival.id").value(testFestivalId))
                    .andExpect(jsonPath("$.performance.time").value("2025-10-10T10:00:00"))
                    .andExpect(jsonPath("$.performance.description").value("Test Performance Description"))
                    .andExpect(jsonPath("$.performance.standardPrice").value(10.00))
                    .andExpect(jsonPath("$.performance.concessionPrice").value(9.00))
                    .andExpect(jsonPath("$.performance.boxOffice").value("Test Box Office"))
                    .andExpect(jsonPath("$.performance.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.performance.updatedAt").isNotEmpty());
        }

        @Test
        void testMandatoryFieldOnlyAddRespondsExpectedPerformanceObject() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProductionId +  ", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.performance.production.id").value(testProductionId))
                    .andExpect(jsonPath("$.performance.venue.id").value(testVenueId))
                    .andExpect(jsonPath("$.performance.festival").isEmpty())
                    .andExpect(jsonPath("$.performance.time").value("2025-10-10T10:00:00"))
                    .andExpect(jsonPath("$.performance.description").isEmpty())
                    .andExpect(jsonPath("$.performance.standardPrice").isEmpty())
                    .andExpect(jsonPath("$.performance.concessionPrice").isEmpty())
                    .andExpect(jsonPath("$.performance.boxOffice").isEmpty())
                    .andExpect(jsonPath("$.performance.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.performance.updatedAt").isNotEmpty());
        }

    }
}
