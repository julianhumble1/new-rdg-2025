package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.*;
import com.rdg.rdg_2025.rdg_2025_spring.repository.*;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import com.rdg.rdg_2025.rdg_2025_spring.utils.AuthTestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class VenueIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VenueRepository venueRepository;

    private static User testAdmin;
    private static String adminToken;

    private static User testUser;
    private static String userToken;

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
    public static void destroyUsers(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST addNewVenue integration tests")
    class PostAddNewVenueIntegrationTests {

        @Test
        void testFullVenueDetailsWithAdminTokenReturns201() throws Exception {
            mockMvc.perform(post("/venues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(
                            "{ " +
                                    "\"name\": \"Test Venue\", " +
                                    "\"notes\": \"Test Notes\", " +
                                    "\"postcode\": \"Test Postcode\", " +
                                    "\"address\": \"Test Address\", " +
                                    "\"town\": \"Test Town\", " +
                                    "\"url\": \"www.test.com\" " +
                                    "}"
                    ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testVenueInDatabaseFollowingSuccessfulAdd() throws Exception {
            // Arrange
            List<Venue> venues = venueRepository.findAll();
            int startingLength = venues.size();

            // Act
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Venue\", " +
                                            "\"notes\": \"Test Notes\", " +
                                            "\"postcode\": \"Test Postcode\", " +
                                            "\"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", " +
                                            "\"url\": \"www.test.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isCreated());

            venues = venueRepository.findAll();
            // Assert
            assertEquals(startingLength + 1, venues.size());
        }

        @Test
        void testVenueNameOthersEmptyWithAdminTokenReturns201() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Venue\", " +
                                            "\"notes\": \"\", " +
                                            "\"postcode\": \"\", " +
                                            "\"address\": \"\", " +
                                            "\"town\": \"\", " +
                                            "\"url\": \"\" " +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testMultipleVenuesWithDifferentNamesCanBeAdded() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Venue\", " +
                                            "\"notes\": \"\", " +
                                            "\"postcode\": \"\", " +
                                            "\"address\": \"\", " +
                                            "\"town\": \"\", " +
                                            "\"url\": \"\" " +
                                            "}"
                            ));

            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Another Test Venue\", " +
                                            "\"notes\": \"\", " +
                                            "\"postcode\": \"\", " +
                                            "\"address\": \"\", " +
                                            "\"town\": \"\", " +
                                            "\"url\": \"\" " +
                                            "}"
                            ))
                            .andExpect(status().isCreated());
        }

        @Test
        void testDuplicateVenueNameResponds409() throws Exception {
            // Arrange
            Venue venue = new Venue("Test Venue", null, null, null, null, null);
            venueRepository.save(venue);

            assertTrue(venueRepository.existsById(venue.getId()));
            // Act & Assert
            mockMvc.perform(post("/venues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(
                            "{ " +
                                    "\"name\": \"Test Venue\", " +
                                    "\"notes\": \"\", " +
                                    "\"postcode\": \"\", " +
                                    "\"address\": \"\", " +
                                    "\"town\": \"\", " +
                                    "\"url\": \"\" " +
                                    "}"
                    ))
                    .andExpect(status().isConflict());


        }

        @Test
        void testVenueNameOthersMissingWithAdminTokenReturns201() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Test Venue\" }"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testMissingVenueNameWithAdminTokenReturns400() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"notes\": \"Test Notes\", " +
                                            "\"postcode\": \"Test Postcode\", " +
                                            "\"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", " +
                                            "\"url\": \"www.test.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEmptyVenueNameWithAdminTokenReturns400() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"\", " +
                                            "\"notes\": \"Test Notes\", " +
                                            "\"postcode\": \"Test Postcode\", " +
                                            "\"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", " +
                                            "\"url\": \"www.test.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testDuplicateNameVenueReturns409() throws Exception {
            Venue venue = new Venue("Test Venue", null, null, null, null, null);
            venueRepository.save(venue);

            mockMvc.perform(post("/venues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(
                            "{ " +
                                    "\"name\": \"Test Venue\", " +
                                    "\"notes\": \"Test Notes\", " +
                                    "\"postcode\": \"Test Postcode\", " +
                                    "\"address\": \"Test Address\", " +
                                    "\"town\": \"Test Town\", " +
                                    "\"url\": \"www.test.com\" " +
                                    "}"
                    ))
                    .andExpect(status().isConflict());
        }

        @Test
        void testMissingTokenReturns401() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Venue\", " +
                                            "\"notes\": \"Test Notes\", " +
                                            "\"postcode\": \"Test Postcode\", " +
                                            "\"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", " +
                                            "\"url\": \"www.test.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testBadTokenReturns401() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "fake token")
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Venue\", " +
                                            "\"notes\": \"Test Notes\", " +
                                            "\"postcode\": \"Test Postcode\", " +
                                            "\"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", " +
                                            "\"url\": \"www.test.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testUserTokenReturns403() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Venue\", " +
                                            "\"notes\": \"Test Notes\", " +
                                            "\"postcode\": \"Test Postcode\", " +
                                            "\"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", " +
                                            "\"url\": \"www.test.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isForbidden());
        }


    }

    @Nested
    @DisplayName("GET getAllVenues integration tests")
    class GetAllVenuesIntegrationTests {

        @BeforeEach
        public void setup() {
            venueRepository.deleteAll();
        }

        @AfterEach
        public void cleanup() {
            venueRepository.deleteAll();
        }

        @Test
        void testSuccessfulGetWithAdminTokenReturns200() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/venues")
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetWithVenuesInDatabaseReturnsVenuesArray() throws Exception {
            // Arrange
            Venue venue = new Venue("Test Venue", null, null, null, null, null);
            venueRepository.save(venue);

            // Act & Assert
            mockMvc.perform(get("/venues")
                    .header("Authorization", adminToken))
                    .andExpect(jsonPath("$.venues").isArray());
        }

        @Test
        void testMissingAdminTokenReturns401() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/venues"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testBadAdminTokenReturns401() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/venues")
                            .header("Authorization", "Fake token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testUserTokenReturns403() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/venues")
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }


    }

    @Nested
    @DisplayName("DELETE deleteVenueById integration tests")
    class DeleteVenueByIdIntegrationTests {

        @Autowired
        private ProductionRepository productionRepository;

        @Autowired
        private FestivalRepository festivalRepository;

        @Autowired
        private PerformanceRepository performanceRepository;

        private Venue testVenue;
        private int testVenueId;

        private Production testProduction;
        private int testProductionId;

        private Festival testFestival;
        private int testFestivalId;

        private Performance testPerformance;
        private int testPerformanceId;

        @BeforeEach
        public void populateDatabase() {

            testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

            testProduction = new Production("Test Production 1", testVenue, null, null, null, false, false, null);
            productionRepository.save(testProduction);
            testProductionId = testProduction.getId();

            testFestival = new Festival("Test Festival 1", testVenue, 2025, 1, "Test Description");
            festivalRepository.save(testFestival);
            testFestivalId = testFestival.getId();

            List<Production> productionList = new ArrayList<>();
            productionList.add(testProduction);
            testVenue.setProductions(productionList);

            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(testFestival);
            testVenue.setFestivals(festivalList);

            testPerformance = new Performance();
            testPerformance.setVenue(testVenue);
            testPerformance.setProduction(testProduction);
            testPerformance.setTime(LocalDateTime.now());
            performanceRepository.save(testPerformance);
            testPerformanceId = testPerformance.getId();

            List<Performance> performanceList = new ArrayList<>();
            performanceList.add(testPerformance);
            testVenue.setPerformances(performanceList);

            venueRepository.save(testVenue);
            testVenueId = testVenue.getId();
        }

        @Test
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testVenueIsNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            assertTrue(venueRepository.existsById(testVenueId));
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", adminToken));
            // Assert
            assertFalse(venueRepository.existsById(testVenueId));
        }

        @Test
        void testAssociatedProductionIsNotDeletedAfterDeletionOfVenue() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", adminToken));

            // Assert
            assertTrue(productionRepository.existsById(testProductionId));
        }

        @Test
        void testAssociatedProductionNoLongerReferencesVenueFollowingDeletion() throws Exception {
            // Arrange
            Production preProduction = productionRepository.findById(testProduction.getId()).orElseThrow(() -> new RuntimeException("No production with this id"));
            assertEquals(testVenue, preProduction.getVenue());
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                    .header("Authorization", adminToken));

            // Assert
            Production postProduction = productionRepository.findById(testProduction.getId()).orElseThrow(() -> new RuntimeException("No production with this id"));
            assertNull(postProduction.getVenue());
        }

        @Test
        void testAssociatedFestivalIsNotDeletedAfterDeletionOfVenue() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                    .header("Authorization", adminToken));
            // Assert
            assertTrue(festivalRepository.existsById(testFestivalId));
        }

        @Test
        void testAssociatedFestivalNoLongerReferencesVenueFollowingDeletion() throws Exception {
            // Arrange
            Festival preFestival = festivalRepository.findById(testFestivalId).orElseThrow(() -> new RuntimeException("No festival with this id"));
            assertEquals(testVenue, preFestival.getVenue());
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                    .header("Authorization", adminToken));
            // Assert
            Festival postFestival = festivalRepository.findById(testFestivalId).orElseThrow(() -> new RuntimeException("No festival with this id"));
            assertNull(postFestival.getVenue());
        }

        @Test
        void testAssociatedPerformanceNoLongerExistsFollowingDeletion() throws Exception {
            // Arrange
            assertTrue(performanceRepository.existsById(testPerformanceId));
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                    .header("Authorization", adminToken));
            // Assert
            assertFalse(performanceRepository.existsById(testPerformanceId));
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/venues/" + (testVenueId - 1))
                            .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testBadVenueIdResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/venues/" + "Bad venue")
                            .header("Authorization", adminToken))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/venues/" + testVenueId))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/venues/" + testVenueId)
                    .header("Authorization", "Bad Token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("GET getVenueById integration tests")
    class GetVenueByIdIntegrationTests {

        private Venue testVenue;
        private int testVenueId;

        @Autowired
        private ProductionRepository productionRepository;

        @Autowired
        private FestivalRepository festivalRepository;

        @BeforeEach
        void addVenue() {
            testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            venueRepository.save(testVenue);
            testVenueId = testVenue.getId();
        }

        @Test
        void testSuccessfulGetReturns200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenueId))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetReturnsExpectedVenue() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenueId))
                    .andExpect(jsonPath("$.venue.name").value("Test Venue"));
        }

        @Test
        void testSuccessfulGetReturnsProductionsArray() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenueId))
                    .andExpect(jsonPath("$.productions").isArray());
        }

        @Test
        void testSuccessfulGetReturnsExpectedProductionInArray() throws Exception {
            // Arrange
            Production testProduction = new Production(
                    "Test Production", testVenue, null, null, null, false, false, null
            );
            productionRepository.save(testProduction);

            List<Production> productionList = new ArrayList<>();
            productionList.add(testProduction);
            testVenue.setProductions(productionList);
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenueId))
                    .andExpect(jsonPath("$.productions[0].name").value("Test Production"));
        }

        @Test
        void testSuccessfulGetReturnsFestivalsArray() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenueId))
                    .andExpect(jsonPath("$.festivals").isArray());
        }

        @Test
        void testSuccessfulGetReturnsExpectedFestivalInArray() throws Exception {
            // Arrange
            Festival testFestival = new Festival("Test Festival", testVenue, 2025, 1, null);
            festivalRepository.save(testFestival);

            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(testFestival);
            testVenue.setFestivals(festivalList);
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenueId))
                    .andExpect(jsonPath("$.festivals[0].name").value("Test Festival"));
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + (testVenueId - 1)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testInvalidVenueIdResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/venues/" + "Bad Venue Id"))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("PATCH updateVenue integration tests")
    class UpdateVenueIntegrationTests {

        private Venue testVenue;
        private int testVenueId;

        @BeforeEach
        void beforeAll(@Autowired VenueRepository venueRepository) {

            venueRepository.deleteAll();

            testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            venueRepository.save(testVenue);
            testVenueId = testVenue.getId();
        }

        @Test
        void testFullDetailsWithAdminTokenResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Venue\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\", " +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isOk());

        }

        @Test
        void testFullDetailsWithAdminTokenRespondsExpectedVenueObject() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Venue\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.venue.name").value("Updated Test Venue"))
                    .andExpect(jsonPath("$.venue.notes").value("Updated Test Notes"))
                    .andExpect(jsonPath("$.venue.postcode").value("Updated Test Postcode"))
                    .andExpect(jsonPath("$.venue.address").value("Updated Test Address"))
                    .andExpect(jsonPath("$.venue.town").value("Updated Test Town"))
                    .andExpect(jsonPath("$.venue.url").value("www.updatedtest.com"))
                    .andExpect(jsonPath("$.venue.slug").value("updated-test-venue"));

        }

        @Test
        void testOnlyNameResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Updated Test Venue\"}"
                            ))
                    .andExpect(status().isOk());

        }

        @Test
        void testEmptyNameResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testMissingNameResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + (testVenueId -1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Venue\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());

        }

        @Test
        void testBadVenueIdResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + "bad venue id")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Venue\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Venue\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" }"
                            ))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bad token")
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Venue\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + testVenueId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Venue\", " +
                                            "\"notes\": \"Updated Test Notes\", " +
                                            "\"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", " +
                                            "\"url\": \"www.updatedtest.com\" " +
                                            "}"
                            ))
                    .andExpect(status().isForbidden());

        }



    }

}
