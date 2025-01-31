package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.User;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                                            "\"venueId\": " + testVenue1.getId() + ", " +
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
        void testDuplicateVenueNameReturnsSlugWithNumberOnEnd() throws Exception {

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
                                            "\"venueId\": " + (testVenue1.getId() - 1) + ", " +
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
                                            "\"venueId\": " + testVenue1.getId() + ", " +
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
                                            "\"venueId\": " + testVenue1.getId() + ", " +
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
                                            "\"venueId\": " + testVenue1.getId() + ", " +
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
                                            "\"venueId\": " + testVenue1.getId() + ", " +
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
                                            "\"venueId\": " + testVenue1.getId() + ", " +
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
                                            "\"venueId\": " + testVenue1.getId() + ", " +
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

        @Test
        void testSuccessfulGetResponds200() throws Exception{
            // Act & Assert
            mockMvc.perform(get("/productions"))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetWithProductionsInDatabaseReturnsProductionsArray() throws Exception {
            // Arrange
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            Production testProduction = new Production(
                    "Test Production",
                    managedTestVenue1, null, null, null, false, false, null
            );
            productionRepository.save(testProduction);

            // Act & Assert
            mockMvc.perform(get("/productions"))
                    .andExpect(jsonPath("$.productions").isArray());

        }

    }

    @Nested
    @DisplayName("GET get production by id integration tests")
    class GetProductionByIdIntegrationTest {

        @Autowired
        VenueRepository venueRepository;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        Production testProduction;

        @BeforeEach
        void beforeEach() {
            productionRepository.deleteAll();
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Failed to find venue"));

            testProduction = new Production(
                    "Test Production",
                    managedTestVenue1,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.parse("2025-10-10T10:00:00", formatter),
                    false,
                    false,
                    "Test File String"
            );
            productionRepository.save(testProduction);
        }

        @AfterEach
        void afterEach() {
            productionRepository.deleteAll();
        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/productions/" + testProduction.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetRespondsExpectedProductionObject() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/productions/" + testProduction.getId()))
                    .andExpect(jsonPath("$.production.name").value("Test Production"))
                    .andExpect(jsonPath("$.production.author").value("Test Author"))
                    .andExpect(jsonPath("$.production.description").value("Test Description"))
                    .andExpect(jsonPath("$.production.sundowners").value(false))
                    .andExpect(jsonPath("$.production.notConfirmed").value(false))
                    .andExpect(jsonPath("$.production.flyerFile").value("Test File String"))
                    .andExpect(jsonPath("$.production.venue.name").value("Test Venue 1"));
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/productions/" + (testProduction.getId() - 1)))
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

        @Autowired
        private VenueRepository venueRepository;

        Production testExistingProduction;

        @BeforeEach
        void beforeEach() {
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            testExistingProduction = new Production(
                    "Test Production",
                    managedTestVenue1,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test Flyer File"
            );
            productionRepository.save(testExistingProduction);
        }

        @Test
        void testSuccessfulUpdateWithFullProductionDetailsResponds200() throws Exception {
            // Arrange
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
                                            "\"author\": \"Updated Test Author\", " +
                                            "\"description\": \"Updated Test Description\", " +
                                            "\"auditionDate\": \"2025-11-10T10:00:00\", " +
                                            "\"sundowners\": true, " +
                                            "\"notConfirmed\": true, " +
                                            "\"flyerFile\": \"Updated Test Flyer File\" " +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.production.name").value("Updated Test Production"))
                    .andExpect(jsonPath("$.production.venue.id").value(managedTestVenue2.getId()))
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
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
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
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Existing Production\", " +
                                            "\"venueId\": " + (managedTestVenue2.getId() + 1) +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testNonExistentProductionIdResponds404() throws Exception {
            // Arrange
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            // Act & Assert
            mockMvc.perform(patch("/productions/" + (testExistingProduction.getId() + 1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Existing Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +
                                            "}"
                            ))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testProductionIdNotIntegerResponds400() throws Exception {
            // Arrange
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + "bad production id")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
            Venue managedTestVenue2 = venueRepository.findById(testVenue2.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            // Act & Assert
            mockMvc.perform(patch("/productions/" + testExistingProduction.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ " +
                                            "\"name\": \"Updated Test Production\", " +
                                            "\"venueId\": " + managedTestVenue2.getId() +  ", " +
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
        private VenueRepository venueRepository;

        Production testExistingProduction;

        @BeforeEach
        void beforeEach() {
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));

            testExistingProduction = new Production(
                    "Test Production",
                    managedTestVenue1,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test Flyer File"
            );
            productionRepository.save(testExistingProduction);
        }

        @Test
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();
            // Act & Assert
            mockMvc.perform(delete("/productions/" + productionId)
                    .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testProductionNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();
            // Act
            mockMvc.perform(delete("/productions/" + productionId)
                            .header("Authorization", adminToken));
            // Assert
            boolean exists = productionRepository.existsById(productionId);
            assertEquals(false, exists);
        }

        @Test
        void testAssociatedVenueNoLongerReferencesProductionFollowingDeletion() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();

            // Act
            mockMvc.perform(delete("/productions/" + productionId)
                    .header("Authorization", adminToken));
            // Assert
            productionRepository.flush();
            Venue managedTestVenue1 = venueRepository.findById(testVenue1.getId()).orElseThrow(() -> new RuntimeException("Venue not found"));
            List<Production> productions = managedTestVenue1.getProductions();
            assertEquals(false, productions.contains(testExistingProduction));
        }

        @Test
        void testNonExistentProductionIdResponds404() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();

            // Act & Assert
            mockMvc.perform(delete("/productions/" + (productionId - 1))
                    .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());

        }

        @Test
        void testProductionIdNotIntResponds400() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();

            // Act & Assert
            mockMvc.perform(delete("/productions/" +  "notanint")
                            .header("Authorization", adminToken))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();

            // Act & Assert
            mockMvc.perform(delete("/productions/" +  productionId)
                            .header("Authorization", "badToken"))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();

            // Act & Assert
            mockMvc.perform(delete("/productions/" +  productionId))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            int productionId = testExistingProduction.getId();

            // Act & Assert
            mockMvc.perform(delete("/productions/" +  productionId)
                    .header("Authorization", userToken))
                    .andExpect(status().isForbidden());

        }


    }
}
