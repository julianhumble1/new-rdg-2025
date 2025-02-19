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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private Venue testVenue;
    private int testVenueId;

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

    @BeforeEach
    public void addVenuesToDatabase(@Autowired VenueRepository venueRepository) {

        testVenue = new Venue("Test Venue 1", null, null, null, null, null);
        venueRepository.save(testVenue);
        testVenueId = testVenue.getId();

    }

    @Nested
    @DisplayName("POST add new festival integration tests")
    class AddNewFestivalIntegrationTests {

        @Test
        void testFullProductionDetailsWithNoVenueReturns201() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testFullFestivalDetailsWithNoVenueReturnsExpectedProductionObject() throws Exception {
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.festival.name").value("Test Festival")
                    );
        }

        @Test
        void testFullProductionDetailsWithValidVenueReturns201() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testProductionInDatabaseFollowingSuccessfulAdd() throws Exception {
            // Arrange
            List<Festival> festivals = festivalRepository.findAll();
            int startingLength = festivals.size();
            // Act
            mockMvc.perform(post("/festivals")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(
                            "{" +
                                    "\"name\": \"Test Festival\", " +
                                    "\"venueId\": " + testVenueId + ", " +
                                    "\"year\": 2025, " +
                                    "\"month\": 1, " +
                                    "\"description\": \"Test Description\"" +
                                    "}"
                    ));
            festivals = festivalRepository.findAll();
            // Assert
            assertEquals(startingLength + 1, festivals.size());

        }

        @Test
        void testOnlyNameAndYearReturns201() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025" +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testInvalidVenueIdResponds404BadRequest() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": " + (testVenueId - 1) + ", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testMissingNameResponds400BadRequest() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEmptyNameResponds400BadRequest() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
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
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": \"Bad Venue Id\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testMissingYearResponds400BadRequest() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEmptyYearResponds400BadRequest() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": \"\", " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testMonthNotIntResponds400BadRequest() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": \"Bad month\", " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testMonthAbove12Responds400BadRequest() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 13, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNegativeMonthResponds400BadRequest() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": -1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bad Token")
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025, \"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isForbidden());
        }



    }

    @Nested
    @DisplayName("GET get all festivals integration tests")
    class GetAllFestivalsIntegrationTests {

        @Autowired
        private VenueRepository venueRepository;

        @Test
        void testSuccessfulGetWithAdminTokenResponds200 () throws Exception {

            // Act & Assert
            mockMvc.perform(get("/festivals")
                            .header("Authorization", adminToken))
                    .andExpect(status().isOk());

        }

        @Test
        void testSuccessfulGetWithFestivalsInDatabaseRespondsExpectedFestivalsArray() throws Exception {
            // Arrange
            Festival testFestival = new Festival("Test Festival", testVenue, 2025, 1, "Test Description");
            festivalRepository.save(testFestival);

            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(testFestival);
            testVenue.setFestivals(festivalList);

            venueRepository.save(testVenue);

            // Act & Assert
            mockMvc.perform(get("/festivals")
                            .header("Authorization", adminToken))
                    .andExpect(jsonPath("$.festivals").isArray())
                    .andExpect(jsonPath("$.festivals[0].name").value("Test Festival"));
        }

        @Test
        void testMissingAdminTokenReturns401() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testBadAdminTokenReturns401() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals")
                            .header("Authorization", "Fake token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testUserTokenReturns403() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals")
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }


    }

    @Nested
    @DisplayName("GET get festival by id integration tests")
    class GetFestivalByIdIntegrationTests {

        @Autowired
        private VenueRepository venueRepository;

        private Festival testFestival;
        private int testFestivalId;

        @BeforeEach
        void beforeEach() {
            testFestival = new Festival("Test Festival", testVenue, 2025, 1, "Test Description");
            festivalRepository.save(testFestival);
            testFestivalId = testFestival.getId();

            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(testFestival);
            testVenue.setFestivals(festivalList);

            venueRepository.save(testVenue);
        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals/" + testFestivalId))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetRespondsExpectedFestivalObject() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals/" + testFestivalId))
                    .andExpect(jsonPath("$.festival.name").value("Test Festival"))
                    .andExpect(jsonPath("$.festival.id").value(testFestivalId))
                    .andExpect(jsonPath("$.festival.year").value(2025))
                    .andExpect(jsonPath("$.festival.month").value(1))
                    .andExpect(jsonPath("$.festival.description").value("Test Description"))
                    .andExpect(jsonPath("$.festival.venue.name").value("Test Venue 1"));
        }

        @Test
        void testNonExistentFestivalIdResponds404() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals/" + (testFestivalId + 1)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testInvalidVenueIdResponds400() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals/notanint"))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("DELETE delete festival by id integration tests")
    class DeleteFestivalByIdIntegrationTests {

        @Autowired
        private VenueRepository venueRepository;

        @Autowired
        private PerformanceRepository performanceRepository;

        @Autowired
        private ProductionRepository productionRepository;

        private Festival testFestival;
        private int testFestivalId;

        @BeforeEach
        void beforeEach() {
            testFestival = new Festival(
                    "Existing Festival",
                    testVenue,
                    2025,
                    1,
                    "Existing Festival Description"
            );

            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(testFestival);
            testVenue.setFestivals(festivalList);

            festivalRepository.save(testFestival);
            testFestivalId = testFestival.getId();

            venueRepository.save(testVenue);
        }

        @Test
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + testFestival.getId())
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testFestivalNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            assertTrue(festivalRepository.existsById(testFestivalId));
            // Act
            mockMvc.perform(delete("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken));
            // Assert
            assertFalse(festivalRepository.existsById(testFestivalId));
        }

        @Test
        void testAssociatedVenueNoLongerReferencesFestivalFollowingDeletion() throws Exception {
            // Arrange
            Venue preVenue = venueRepository.findById(testVenue.getId()).orElseThrow(() -> new RuntimeException("No venue with this id"));
            assertTrue(preVenue.getFestivals().contains(testFestival));
            // Act
            mockMvc.perform(delete("/festivals/" + testFestivalId)
                    .header("Authorization", adminToken));
            // Assert
            Venue postVenue = venueRepository.findById(testVenue.getId()).orElseThrow(() -> new RuntimeException("No venue with this id"));
            assertFalse(postVenue.getFestivals().contains(testFestival));
        }


        @Test
        void testAssociatedPerformanceNoLongerReferencesFestivalFollowingDeletion() throws Exception {
            // Arrange
            Production production = new Production(
                    "Test Production",
                    testVenue,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test File String"
            );
            productionRepository.save(production);

            Performance associatedPerformance = new Performance();
            associatedPerformance.setVenue(testVenue);
            associatedPerformance.setProduction(production);
            associatedPerformance.setFestival(testFestival);
            associatedPerformance.setTime(LocalDateTime.now());
            performanceRepository.save(associatedPerformance);
            List<Performance> performanceList = new ArrayList<>();
            performanceList.add(associatedPerformance);
            testFestival.setPerformances(performanceList);
            festivalRepository.save(testFestival);

            assertEquals(testFestival, associatedPerformance.getFestival());
            // Act
            mockMvc.perform(delete("/festivals/" + testFestivalId)
                    .header("Authorization", adminToken));
            // Assert
            assertNull(associatedPerformance.getFestival());
        }

        @Test
        void testNonExistentFestivalIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + (testFestivalId - 1) )
                            .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testFestivalIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + "not an integer" )
                            .header("Authorization", adminToken))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + testFestivalId )
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + testFestivalId )
                            .header("Authorization", "bad token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + testFestivalId ))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    @DisplayName("PATCH update festival integration tests")
    class UpdateFestivalIntegrationTests {

        private Festival testFestival;
        private int testFestivalId;

        private Venue testVenue2;
        private int testVenue2Id;

        @Autowired
        private VenueRepository venueRepository;

        @BeforeEach
        void beforeEach() {
            testVenue2 = new Venue("Test Venue 2", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            venueRepository.save(testVenue2);
            testVenue2Id = testVenue2.getId();

            testFestival = new Festival("Test Festival", testVenue, 2025, 1, "Test Description");
            festivalRepository.save(testFestival);
            testFestivalId = testFestival.getId();

            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(testFestival);
            testFestivalId = testFestival.getId();
            testVenue.setFestivals(festivalList);
            venueRepository.save(testVenue);

        }

        @Test
        void testSuccessfulUpdateResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isOk());

        }

        @Test
        void testSuccessfulUpdateRespondsExpectedFestivalObject() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.festival.name").value("Updated Test Festival"))
                    .andExpect(jsonPath("$.festival.venue.name").value("Test Venue 2"))
                    .andExpect(jsonPath("$.festival.year").value(2026))
                    .andExpect(jsonPath("$.festival.month").value(2))
                    .andExpect(jsonPath("$.festival.description").value("Updated Test Description"));

        }

        @Test
        void testFestivalInDatabaseHasBeenUpdated() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ));
            // Assert
            Festival festival = festivalRepository.findById(testFestivalId).orElseThrow(() -> new RuntimeException("festival can't be found"));
            assertEquals("Updated Test Festival", festival.getName());
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                    .header("Authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{" +
                                    "\"name\": \"Updated Test Festival\"," +
                                    "\"venueId\": "+ (testVenue2Id + 1) +", " +
                                    "\"year\": 2026, " +
                                    "\"month\": 2, " +
                                    "\"description\": \"Updated Test Description\"" +
                                    "}"
                    ))
                    .andExpect(status().isNotFound());

        }

        @Test
        void testNonExistentFestivalIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + (testFestivalId + 1))
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());

        }

        @Test
        void testFestivalIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/notanint")
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testNameEmptyResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testNameMissingResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testVenueIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            " \"venueId\": \"not an int\", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testYearNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": \"not an integer\", " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testYearBlankResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": \"\", " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testYearMissingResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testMonthBelowZeroResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": -1, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testMonthAboveTwelveResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ testVenue2Id +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 13, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testUpdateWithOnlyMandatoryFieldsResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + testFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"year\": 2026 " +
                                            "}"
                            ))
                    .andExpect(status().isOk());

        }


    }
}
