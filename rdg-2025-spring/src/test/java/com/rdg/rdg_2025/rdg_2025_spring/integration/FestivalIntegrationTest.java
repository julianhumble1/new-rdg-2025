package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.*;
import com.rdg.rdg_2025.rdg_2025_spring.repository.*;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import com.rdg.rdg_2025.rdg_2025_spring.utils.AuthTestUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.List;
import java.util.Optional;

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
            int testVenueId = testVenue1.getId();

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
            int testVenueId = testVenue1.getId();
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
            int testVenueId = testVenue1.getId();
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
        void testSuccessfulGetWithFestivalsInDatabaseRespondsFestivalsArray() throws Exception {
            // Arrange
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            Festival testFestival = new Festival("Test Festival", managedTestVenue1, 2025, 1, "Test Description");
            festivalRepository.save(testFestival);

            // Act & Assert
            mockMvc.perform(get("/festivals")
                            .header("Authorization", adminToken))
                    .andExpect(jsonPath("$.festivals").isArray());
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

        @BeforeEach
        void beforeEach() {
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            testFestival = new Festival("Test Festival", managedTestVenue1, 2025, 1, "Test Description");
            festivalRepository.save(testFestival);
        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals/" + testFestival.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetRespondsExpectedFestivalObject() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals/" + testFestival.getId()))
                    .andExpect(jsonPath("$.festival.name").value("Test Festival"))
                    .andExpect(jsonPath("$.festival.id").value(testFestival.getId()))
                    .andExpect(jsonPath("$.festival.year").value(2025))
                    .andExpect(jsonPath("$.festival.month").value(1))
                    .andExpect(jsonPath("$.festival.description").value("Test Description"))
                    .andExpect(jsonPath("$.festival.venue.name").value("Test Venue 1"));
        }

        @Test
        void testNonExistentFestivalIdResponds404() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/festivals/" + (testFestival.getId() + 1)))
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

        @Autowired
        private EntityManager entityManager;

        Festival testExistingFestival;

        @BeforeEach
        void beforeEach() {
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            testExistingFestival = new Festival(
                    "Existing Festival",
                    managedTestVenue1,
                    2025,
                    1,
                    "Existing Festival Description"
            );

            festivalRepository.save(testExistingFestival);
        }

        @Test
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId())
                    .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testFestivalNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId())
                            .header("Authorization", adminToken));
            // Assert
            festivalRepository.flush();

            boolean exists = festivalRepository.existsById(testExistingFestival.getId());
            assertFalse(exists);
        }

        @Test
        void testAssociatedVenueStillInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId())
                    .header("Authorization", adminToken));
            // Assert
            festivalRepository.flush();
            boolean exists = venueRepository.existsById(testVenue1.getId());
            assertTrue(exists);
        }

        @Test
        void testAssociatedVenueNoLongerReferencesFestivalFollowingDeletion() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId())
                    .header("Authorization", adminToken));
            // Assert
            festivalRepository.flush();
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            List<Festival> festivalList = managedTestVenue1.getFestivals();
            assertFalse(festivalList.contains(testExistingFestival));
        }

        @Test
        void testAssociatedPerformanceStillInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            Production production = new Production(
                    "Test Production",
                    testVenue1,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test File String"
            );
            productionRepository.save(production);

            Performance performance = new Performance(
                    production,
                    testVenue1,
                    testExistingFestival,
                    LocalDateTime.now(),
                    "Test Description",
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    "box office"
            );
            performanceRepository.save(performance);
            // Act
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId())
                    .header("Authorization", adminToken));
            // Assert
            boolean exists = performanceRepository.existsById(performance.getId());
            assertTrue(exists);

        }

        @Test
        void testNonExistentFestivalIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + (testExistingFestival.getId() - 1) )
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
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId() )
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId() )
                            .header("Authorization", "bad token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/" + testExistingFestival.getId() ))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    @DisplayName("PATCH update festival integration tests")
    class UpdateFestivalIntegrationTests {

        private Festival existingFestival;
        int existingFestivalId;

        @Autowired
        VenueRepository venueRepository;

        Venue managedTestVenue1;
        Venue managedTestVenue2;

        @BeforeEach
        void beforeEach() {
            managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("no venue with this id"));

            existingFestival = new Festival("Test Festival", managedTestVenue1, 2025, 1, "Test Description");
            festivalRepository.save(existingFestival);
            existingFestivalId = existingFestival.getId();

        }

        @Test
        void testSuccessfulUpdateResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/" + existingFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ managedTestVenue2.getId() +", " +
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
            mockMvc.perform(patch("/festivals/" + existingFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ managedTestVenue2.getId() +", " +
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
            mockMvc.perform(patch("/festivals/" + existingFestivalId)
                            .header("Authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            "\"venueId\": "+ managedTestVenue2.getId() +", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            ));
            // Assert
            Festival festival = festivalRepository.findById(existingFestivalId).orElseThrow(() -> new RuntimeException("festival can't be found"));
            assertEquals("Updated Test Festival", festival.getName());
        }

    }
}
