package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.models.*;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private VenueRepository venueRepository;

    @Autowired
    private ProductionRepository productionRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private MockMvc mockMvc;

    private static User testAdmin;
    private static String adminToken;

    private static User testUser;
    private static String userToken;

    private Venue testVenue1;
    private int testVenue1Id;

    private Production testProduction1;
    private int testProduction1Id;

    private Festival testFestival1;
    private int testFestival1Id;

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
    public void populateDatabase() {

        testVenue1 = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

        testProduction1 = new Production(
                "Test Production",
                testVenue1,
                "Test Author",
                "Test Description",
                LocalDateTime.parse("2025-10-10T10:00:00", formatter),
                false,
                false,
                "Test File String"
        );

        testFestival1 = new Festival("Test Festival", testVenue1, 2025, 1, "Test Description");

        venueRepository.save(testVenue1);
        testVenue1Id = testVenue1.getId();
        productionRepository.save(testProduction1);
        testProduction1Id = testProduction1.getId();
        festivalRepository.save(testFestival1);
        testFestival1Id = testFestival1.getId();

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
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
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
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
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
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
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
                                    "\"productionId\": " + testProduction1Id +  ", " +
                                    "\"venueId\": " + testVenue1Id + ", " +
                                    "\"festivalId\": " + testFestival1Id + ", " +
                                    "\"time\": \"2025-10-10T10:00:00\", " +
                                    "\"description\": \"Test Performance Description\", " +
                                    "\"standardPrice\": \"10.00\", " +
                                    "\"concessionPrice\": \"9.00\", " +
                                    "\"boxOffice\": \"Test Box Office\" " +
                                    "}"
                    ))
                    .andExpect(jsonPath("$.performance.production.id").value(testProduction1Id))
                    .andExpect(jsonPath("$.performance.venue.id").value(testVenue1Id))
                    .andExpect(jsonPath("$.performance.festival.id").value(testFestival1Id))
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
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.performance.production.id").value(testProduction1Id))
                    .andExpect(jsonPath("$.performance.venue.id").value(testVenue1Id))
                    .andExpect(jsonPath("$.performance.festival").isEmpty())
                    .andExpect(jsonPath("$.performance.time").value("2025-10-10T10:00:00"))
                    .andExpect(jsonPath("$.performance.description").isEmpty())
                    .andExpect(jsonPath("$.performance.standardPrice").isEmpty())
                    .andExpect(jsonPath("$.performance.concessionPrice").isEmpty())
                    .andExpect(jsonPath("$.performance.boxOffice").isEmpty())
                    .andExpect(jsonPath("$.performance.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.performance.updatedAt").isNotEmpty());
        }

        @Test
        void testNonExistentFestivalIdResponds404() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + (testFestival1Id + 1) + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentProductionIdResponds404() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " +( testProduction1Id + 1) +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + (testVenue1Id + 1)+ ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testProductionIdNotIntResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": \"not an int\", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testProductionIdBlankResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": \"\", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testProductionIdMissingResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testVenueIdNotIntResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": \"not an int\", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testVenueIdBlankResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": \"\", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testVenueIdMissingResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testFestivalIdNotIntResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": \"not an int\", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testTimeNotDateTimeResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"not a date time\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testTimeEmptyResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testTimeMissingResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testStandardPriceNotNumberResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"not a number\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testConcessionPriceNotNumberResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"not a number\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "bad token")
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"productionId\": " + testProduction1Id +  ", " +
                                            "\"venueId\": " + testVenue1Id + ", " +
                                            "\"festivalId\": " + testFestival1Id + ", " +
                                            "\"time\": \"2025-10-10T10:00:00\", " +
                                            "\"description\": \"Test Performance Description\", " +
                                            "\"standardPrice\": \"10.00\", " +
                                            "\"concessionPrice\": \"9.00\", " +
                                            "\"boxOffice\": \"Test Box Office\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    @DisplayName("DELETE delete performance by id integration tests")
    class DeletePerformanceByIdIntegrationTests {

        private Performance testPerformance;
        private int testPerformanceId;

        @BeforeEach
        void setup() {
            testPerformance = new Performance();
            List<Performance> performanceList = new ArrayList<>();
            performanceList.add(testPerformance);

            testPerformance.setVenue(testVenue1);
            testVenue1.setPerformances(performanceList);

            testPerformance.setProduction(testProduction1);
            testProduction1.setPerformances(performanceList);

            testPerformance.setFestival(testFestival1);
            testFestival1.setPerformances(performanceList);

            testPerformance.setTime(LocalDateTime.now());

            performanceRepository.save(testPerformance);
            testPerformanceId = testPerformance.getId();

            venueRepository.save(testVenue1);
            productionRepository.save(testProduction1);
            festivalRepository.save(testFestival1);

        }

        @Test
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/performances/" + testPerformanceId)
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testPerformanceNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/performances/" + testPerformanceId)
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
            // Assert
            assertFalse(performanceRepository.existsById(testPerformanceId));
        }

        @Test
        void testVenueNoLongerReferencesPerformanceFollowingDeletion() throws Exception {
            // Arrange
            Venue preVenue = venueRepository.findById(testVenue1Id).orElseThrow(() -> new RuntimeException("No Venue with this id"));
            assertTrue(preVenue.getPerformances().contains(testPerformance));
            // Act
            mockMvc.perform(delete("/performances/" + testPerformanceId)
                            .header("Authorization", adminToken));
            // Assert
            Venue postVenue = venueRepository.findById(testVenue1Id).orElseThrow(() -> new RuntimeException("No Venue with this id"));
            assertFalse(postVenue.getPerformances().contains(testPerformance));
        }

        @Test
        void testProductionNoLongerReferencesPerformanceFollowingDeletion() throws Exception {
            // Arrange
            Production preProduction = productionRepository.findById(testProduction1Id).orElseThrow(() -> new RuntimeException("No Production with this id"));
            assertTrue(preProduction.getPerformances().contains(testPerformance));
            // Act
            mockMvc.perform(delete("/performances/" + testPerformanceId)
                    .header("Authorization", adminToken));
            // Assert
            Production postProduction = productionRepository.findById(testProduction1Id).orElseThrow(() -> new RuntimeException("No Production with this id"));
            assertFalse(postProduction.getPerformances().contains(testPerformance));
        }

        @Test
        void testFestivalNoLongerReferencesPerformanceFollowingDeletion() throws Exception {
            // Arrange
            Festival preFestival = festivalRepository.findById(testFestival1Id).orElseThrow(() -> new RuntimeException("No Festival with this id"));
            assertTrue(preFestival.getPerformances().contains(testPerformance));
            // Act
            mockMvc.perform(delete("/performances/" + testPerformanceId)
                    .header("Authorization", adminToken));
            // Assert
            Festival postFestival = festivalRepository.findById(testFestival1Id).orElseThrow(() -> new RuntimeException("No Festival with this id"));
            assertFalse(postFestival.getPerformances().contains(testPerformance));
        }

        @Test
        void testNonExistentPerformanceIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/performances/" + (testPerformanceId - 1))
                            .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testBadPerformanceIdResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/performances/notanint" )
                            .header("Authorization", adminToken))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/performances/" + testPerformanceId )
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/performances/" + testPerformanceId )
                            .header("Authorization", "bad token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/performances/" + testPerformanceId ))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    @DisplayName("PATCH update performance integration tests")
    class UpdatePerformanceIntegrationTests {

        private Venue testVenue2;
        private int testVenue2Id;

        private Production testProduction2;
        private int testProduction2Id;

        private Festival testFestival2;
        private int testFestival2Id;

        private Performance testPerformance;
        private int testPerformanceId;

        @Autowired
        private ObjectMapper objectMapper;

        private ObjectNode requestJson;

        @BeforeEach
        public void populateDatabase() {

            testVenue2 = new Venue("Another Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

            testProduction2 = new Production(
                    "Another Test Production",
                    testVenue2,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.parse("2025-10-10T10:00:00", formatter),
                    false,
                    false,
                    "Test File String"
            );

            testFestival2 = new Festival("Test Festival", testVenue2, 2025, 1, "Test Description");

            venueRepository.save(testVenue2);
            testVenue2Id = testVenue2.getId();
            productionRepository.save(testProduction2);
            testProduction2Id = testProduction2.getId();
            festivalRepository.save(testFestival2);
            testFestival2Id = testFestival2.getId();

            testPerformance = new Performance(
                testProduction1,
                testVenue1,
                testFestival1,
                LocalDateTime.now(),
                "Test Description",
                BigDecimal.TEN,
                BigDecimal.valueOf(9),
                "Test Box Office"
            );
            List<Performance> performanceList = new ArrayList<>();
            performanceList.add(testPerformance);

            performanceRepository.save(testPerformance);
            testPerformanceId = testPerformance.getId();

            testProduction1.setPerformances(performanceList);
            testVenue1.setPerformances(performanceList);
            testFestival1.setPerformances(performanceList);

            productionRepository.save(testProduction1);
            venueRepository.save(testVenue1);
            festivalRepository.save(testFestival1);

            requestJson = objectMapper.createObjectNode();
            requestJson.put("productionId", testProduction2Id);
            requestJson.put("venueId", testVenue2Id);
            requestJson.put("festivalId", testFestival2Id);
            requestJson.put("description", "Updated Test Description");
            requestJson.put("time", String.valueOf(LocalDateTime.now()));
            requestJson.put("standardPrice", 11.00);
            requestJson.put("concessionPrice", 10.00);
            requestJson.put("boxOffice", "Updated Test Box Office");
        }

        @Test
        void testSuccessfulUpdateResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulUpdateRespondsExpectedJson() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.performance.production.id").value(testProduction2Id))
                    .andExpect(jsonPath("$.performance.venue.id").value(testVenue2Id))
                    .andExpect(jsonPath("$.performance.festival.id").value(testFestival2Id))
                    .andExpect(jsonPath("$.performance.description").value("Updated Test Description"))
                    .andExpect(jsonPath("$.performance.standardPrice").value(11.00))
                    .andExpect(jsonPath("$.performance.concessionPrice").value(10.00))
                    .andExpect(jsonPath("$.performance.boxOffice").value("Updated Test Box Office"));
        }

        @Test
        void testPerformanceIsUpdatedInDatabase() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
            // Assert
            assertTrue(performanceRepository.findById(testPerformanceId).get().getDescription().equals("Updated Test Description"));
        }

        @Test
        void testOnlyMandatoryFieldsNotEmptyResponds200() throws Exception {
            // Arrange
            requestJson.put("festivalId", "");
            requestJson.put("standardPrice", "");
            requestJson.put("concessionPrice", "");
            requestJson.put("boxOffice", "");
            requestJson.put("description", "");
            // Act & Assert
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("performance.festival").isEmpty());

        }

        @Test
        void testOnlyMandatoryFieldsNotMissingResponds200() throws Exception {
            // Arrange
            requestJson.remove("festivalId");
            requestJson.remove("standardPrice");
            requestJson.remove("concessionPrice");
            requestJson.remove("boxOffice");
            requestJson.remove("description");
            // Act & Assert
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("performance.festival").isEmpty());

        }

        @Test
        void testNonExistentFestivalIdResponds404() throws Exception {
            // Arrange
            assertFalse(festivalRepository.existsById(testFestival2Id + 1));
            requestJson.put("festivalId", testFestival2Id + 1);
            // Act & Assert
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentProductionIdResponds404() throws Exception {
            // Arrange
            assertFalse(productionRepository.existsById(testProduction2Id + 1));
            requestJson.put("productionId", testProduction2Id + 1);
            // Act & Assert
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            assertFalse(venueRepository.existsById(testVenue2Id + 1));
            requestJson.put("venueId", testVenue2Id + 1);
            // Act & Assert
            mockMvc.perform(patch("/performances/" + testPerformanceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentPerformanceIdResponds404() throws Exception {
            // Arrange
            assertFalse(performanceRepository.existsById(testPerformanceId + 1));
            // Act & Assert
            mockMvc.perform(patch("/performances/" + (testPerformanceId + 1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testPerformanceIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/performances/notanint")
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
            mockMvc.perform(patch("/performances/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

    }
}
