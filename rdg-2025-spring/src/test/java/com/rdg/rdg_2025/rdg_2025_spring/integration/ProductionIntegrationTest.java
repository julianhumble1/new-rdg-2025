package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.User;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
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
public class ProductionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductionRepository productionRepository;

    @Autowired
    private VenueRepository venueRepository;

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
    public static void destroyUsers(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @BeforeEach
    public void addVenuesToDatabase() {
        testVenue = new Venue("Test Venue 1", null, null, null, null, null);
        venueRepository.save(testVenue);
        testVenueId = testVenue.getId();
    }

    @Nested
    @DisplayName("POST add new production integration tests")
    class AddNewProductionIntegrationTests {

        @Test
        void testFullProductionDetailsWithNoVenueReturns201() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isCreated()
                    );
        }

        @Test
        void testProductionInDatabaseFollowingSuccessfulAdd() throws Exception {
            // Arrange
            List<Production> productions = productionRepository.findAll();
            int startingLength = productions.size();

            // Act
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isCreated()
                    );
            productions = productionRepository.findAll();
            // Assert
            assertEquals(startingLength + 1, productions.size());
        }

        @Test
        void testFullProductionDetailsWithNoVenueReturnsExpectedProductionObject() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.production.name").value("Test Production")
                    );
        }

        @Test
        void testFullProductionDetailsWithValidVenueReturns201() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isCreated()
                    );
        }

        @Test
        void testOnlyNameReturns201() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Test Production\"}"
                            ))
                    .andExpect(status().isCreated()
                    );
        }

        @Test
        void testDuplicateVenueNameReturnsNameWithNumberOnEnd() throws Exception {
            // Arrange
            Production existingProduction = new Production(
                    "Test Production",
                    null, null, null, null, false, false, null
            );
            productionRepository.save(existingProduction);
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.production.name").value("Test Production (2)")
                    );
        }

        @Test
        void testDuplicateProductionNameReturnsSlugWithNumberOnEnd() throws Exception {
            // Arrange
            Production existingProduction = new Production(
                    "Test Production",
                    null, null, null, null, false, false, null
            );
            productionRepository.save(existingProduction);
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.production.slug").value("test-production-2")
                    );
        }

        @Test
        void testInvalidVenueIdResponds400BadRequest() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + (testVenueId - 1) + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        void testVenueIdNotIntReturns400BadRequest() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": \"Bad Venue Id\", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        void testAuditionDateNotDateResponds400BadRequest() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"Bad Date\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        void testSundownersNotBooleanResponds400BadRequest() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": \"Bad Boolean\", " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        void testNotConfirmedNotBooleanResponds400BadRequest() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": \"Bad Boolean\", " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized()
                    );
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bad Token")
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized()
                    );
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Test Production\", " +
                                            "\"venueId\": " + testVenueId + ", " +
                                            "\"author\": \"Test Author\", " +
                                            "\"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", " +
                                            "\"sundowners\": false, " +
                                            "\"notConfirmed\": false, " +
                                            "\"flyerFile\": \"Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isForbidden()
                    );
        }
    }

    @Nested
    @DisplayName("GET get all productions integration tests")
    class GetAllProductionsIntegrationTests {

        @Autowired
        private VenueRepository venueRepository;

        private Production testProduction;

        @Test
        void testSuccessfulGetResponds200() throws Exception{
            // Act & Assert
            mockMvc.perform(get("/productions"))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetWithProductionsInDatabaseReturnsExpectedProductionsArray() throws Exception {
            // Arrange
            testProduction = new Production(
                    "Test Production",
                    testVenue, null, null, null, false, false, null
            );
            productionRepository.save(testProduction);

            List<Production> productionList = new ArrayList<>();
            productionList.add(testProduction);
            testVenue.setProductions(productionList);
            venueRepository.save(testVenue);

            // Act & Assert
            mockMvc.perform(get("/productions"))
                    .andExpect(jsonPath("$.productions").isArray())
                    .andExpect(jsonPath("$.productions[0].name").value("Test Production"));

        }

    }

    @Nested
    @DisplayName("GET get production by id integration tests")
    class GetProductionByIdIntegrationTest {

        @Autowired
        private VenueRepository venueRepository;

        private Production testProduction;
        private int testProductionId;

        @BeforeEach
        void beforeEach() {
            testProduction = new Production(
                    "Test Production",
                    testVenue,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test File String"
            );
            productionRepository.save(testProduction);
            testProductionId = testProduction.getId();

            List<Production> productionList = new ArrayList<>();
            productionList.add(testProduction);
            testVenue.setProductions(productionList);
            venueRepository.save(testVenue);
        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/productions/" + testProductionId))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetRespondsExpectedProductionObject() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/productions/" + testProductionId))
                    .andExpect(jsonPath("$.production.name").value("Test Production"))
                    .andExpect(jsonPath("$.production.author").value("Test Author"))
                    .andExpect(jsonPath("$.production.description").value("Test Description"))
                    .andExpect(jsonPath("$.production.auditionDate").isNotEmpty())
                    .andExpect(jsonPath("$.production.sundowners").value(false))
                    .andExpect(jsonPath("$.production.notConfirmed").value(false))
                    .andExpect(jsonPath("$.production.flyerFile").value("Test File String"))
                    .andExpect(jsonPath("$.production.venue.name").value("Test Venue 1"));
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/productions/" + (testProductionId - 1)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testBadVenueIdResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/productions/badVenueId"))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("PATCH update production integration tests")
    class UpdateProductionIntegrationTests {

        private Production testProduction;
        private int testProductionId;

        private Venue testVenue2;
        private int testVenue2Id;

        @BeforeEach
        void beforeEach() {
            testProduction = new Production(
                    "Test Production",
                    testVenue,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test Flyer File"
            );
            productionRepository.save(testProduction);
            testProductionId = testProduction.getId();

            List<Production> productionList = new ArrayList<>();
            productionList.add(testProduction);
            testVenue.setProductions(productionList);
            venueRepository.save(testVenue);

            testVenue2 = new Venue("Test Venue 2", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            venueRepository.save(testVenue2);
            testVenue2Id = testVenue2.getId();

        }

        @Test
        void testSuccessfulUpdateWithFullProductionDetailsResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulUpdateWithFullProductionDetailsRespondsExpectedProductionObject() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" +testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.production.name").value("Updated Test Production"))
                    .andExpect(jsonPath("$.production.venue.id").value(testVenue2Id))
                    .andExpect(jsonPath("$.production.author").value("Updated Test Author"))
                    .andExpect(jsonPath("$.production.description").value("Updated Test Description"))
                    .andExpect(jsonPath("$.production.auditionDate").value("2025-11-10T10:00:00"))
                    .andExpect(jsonPath("$.production.sundowners").value(true))
                    .andExpect(jsonPath("$.production.notConfirmed").value(true))
                    .andExpect(jsonPath("$.production.flyerFile").value("Updated Test Flyer File"))
                    .andExpect(jsonPath("$.production.slug").value("updated-test-production"));

        }

        @Test
        void testSuccessfulUpdateOnlyNameRespondsExpectedProductionObject() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Updated Test Production\" }"
                            ))
                    .andExpect(jsonPath("$.production.name").value("Updated Test Production"))
                    .andExpect(jsonPath("$.production.venue").isEmpty())
                    .andExpect(jsonPath("$.production.author").isEmpty())
                    .andExpect(jsonPath("$.production.description").isEmpty())
                    .andExpect(jsonPath("$.production.auditionDate").isEmpty())
                    .andExpect(jsonPath("$.production.sundowners").value(false))
                    .andExpect(jsonPath("$.production.notConfirmed").value(false))
                    .andExpect(jsonPath("$.production.flyerFile").isEmpty())
                    .andExpect(jsonPath("$.production.slug").value("updated-test-production"));
        }

        @Test
        void testUpdateToExistingProductionNameRespondsNameWithBrackets() throws Exception {
            // Arrange
            Production existingProduction = new Production(
                    "Existing Production", null, null, null, null, false, false, null
            );
            productionRepository.save(existingProduction);
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Existing Production\" }"
                            ))
                    .andExpect(jsonPath("$.production.name").value("Existing Production (2)"))
                    .andExpect(jsonPath("$.production.slug").value("existing-production-2"));
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Existing Production\", " +
                                            "\"venueId\": " + (testVenue2Id + 1) +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentProductionIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + (testProductionId + 1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Existing Production\", " +
                                            "\"venueId\": " + testVenue2Id +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testProductionIdNotIntegerResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + "bad production id")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdMissingResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testVenueIdNotIntegerResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + "\"not an integer\"" +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testAuditionDateNotDateResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"not a date\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testSundownersNotBooleanResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": \"not a boolean\", " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNotConfirmedNotBooleanResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": \"not a boolean\", " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNameBlankResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNameMissingResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testProductionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + testVenue2Id +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    @DisplayName("DELETE delete production by id integration tests")
    class DeleteProductionByIdIntegrationTests {

        @Autowired
        private PerformanceRepository performanceRepository;

        private Production testProduction;
        private int testProductionId;

        @BeforeEach
        void beforeEach() {

            testProduction = new Production(
                    "Test Production",
                    testVenue,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test Flyer File"
            );

            List<Production> productionList = new ArrayList<>();
            productionList.add(testProduction);
            testVenue.setProductions(productionList);

            productionRepository.save(testProduction);
            testProductionId = testProduction.getId();

            venueRepository.save(testVenue);
        }

        @Test
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/productions/" + testProductionId)
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testProductionNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            assertTrue(productionRepository.existsById(testProductionId));
            // Act
            mockMvc.perform(delete("/productions/" + testProductionId)
                            .header("Authorization", adminToken));
            // Assert
            assertFalse(productionRepository.existsById(testProductionId));
        }

        @Test
        void testAssociatedVenueNoLongerReferencesProductionFollowingDeletion() throws Exception {
            // Arrange
            Venue preVenue = venueRepository.findById(testVenueId).orElseThrow(() -> new RuntimeException("No venue with this id"));
            assertTrue(preVenue.getProductions().contains(testProduction));
            // Act
            mockMvc.perform(delete("/productions/" + testProductionId)
                    .header("Authorization", adminToken));
            // Assert
            Venue postVenue = venueRepository.findById(testVenueId).orElseThrow(() -> new RuntimeException("No venue with this id"));
            assertFalse(postVenue.getProductions().contains(testProduction));
        }

        @Test
        void testAssociatedPerformancesNoLongerExistFollowingDeletion() throws Exception {
            // Arrange
            Performance associatedPerformance = new Performance();
            associatedPerformance.setVenue(testVenue);
            associatedPerformance.setProduction(testProduction);
            associatedPerformance.setTime(LocalDateTime.now());
            performanceRepository.save(associatedPerformance);
            List<Performance> performanceList = new ArrayList<>();
            performanceList.add(associatedPerformance);
            testProduction.setPerformances(performanceList);
            productionRepository.save(testProduction);

            assertTrue(performanceRepository.existsById(associatedPerformance.getId()));
            // Act
            mockMvc.perform(delete("/productions/" + testProductionId)
                    .header("Authorization", adminToken));
            // Assert
            assertFalse(performanceRepository.existsById(associatedPerformance.getId()));
        }

        @Test
        void testNonExistentProductionIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/productions/" + (testProductionId - 1))
                    .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());

        }

        @Test
        void testProductionIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/productions/" +  "notanint")
                            .header("Authorization", adminToken))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/productions/" +  testProductionId)
                            .header("Authorization", "badToken"))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/productions/" +  testProductionId))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/productions/" +  testProductionId)
                    .header("Authorization", userToken))
                    .andExpect(status().isForbidden());

        }


    }
}
