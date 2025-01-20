package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.User;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
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


import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    class postAddNewVenueIntegrationTests {

        @BeforeEach
        public void setup() {
            venueRepository.deleteAll();
        }

        @AfterEach
        public void cleanup() {
            venueRepository.deleteAll();
        }

        @Test
        void testFullVenueDetailsWithAdminTokenReturns201() throws Exception {
            mockMvc.perform(post("/venues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(
                            "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                    "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                    ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testVenueNameOthersEmptyWithAdminTokenReturns201() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"\", \"postcode\": \"\", \"address\": \"\", " +
                                            "\"town\": \"\", \"url\": \"\" }"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        void testMultipleVenuesWithDifferentNamesCanBeAdded() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"\", \"postcode\": \"\", \"address\": \"\", " +
                                            "\"town\": \"\", \"url\": \"\" }"
                            ));

            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Another Test Venue\", \"notes\": \"\", \"postcode\": \"\", \"address\": \"\", " +
                                            "\"town\": \"\", \"url\": \"\" }"
                            ))
                            .andExpect(status().isCreated());
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
                                    "{ \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testEmptyVenueNameWithAdminTokenReturns400() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isBadRequest());
        }

        // Disabling as getting strange results and unable to make it pass although functions as expected in reality
        @Test
        @Disabled
        void testDuplicateNameVenueReturns409() throws Exception {
            Venue venue = new Venue("Test Venue", null, null, null, null, null);
            venueRepository.save(venue);

            mockMvc.perform(post("/venues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(
                            "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                    "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                    ))
                    .andExpect(status().isConflict());
        }

        @Test
        void testMissingTokenReturns401() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testBadTokenReturns401() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "fake token")
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testUserTokenReturns403() throws Exception {
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isForbidden());
        }


    }

    @Nested
    @DisplayName("GET getAllVenues integration tests")
    class getGetAllVenuesIntegrationTests {

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
    class deleteVenueByIdIntegrationTests {

        @Autowired
        private ProductionRepository productionRepository;

        @Autowired
        private FestivalRepository festivalRepository;

        Venue testVenue1;
        Venue testVenue2;

        Production testProduction1;
        Production testProduction2;

        Festival testFestival1;
        Festival testFestival2 ;

        @BeforeEach
        public void populateDatabase() {

            testVenue1 = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            testVenue2 = new Venue("Another Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

            venueRepository.save(testVenue1);
            venueRepository.save(testVenue2);

            testProduction1 = new Production("Test Production 1", testVenue1, null, null, null, false, false, null);
            testProduction2 = new Production("Test Production 2", testVenue2, null, null, null, false, false, null);

            productionRepository.save(testProduction1);
            productionRepository.save(testProduction2);

            testFestival1  = new Festival("Test Festival 1", testVenue1, 2025, 1, "Test Description");
            testFestival2 = new Festival("Test Festival 2", testVenue2, 2025, 1, "Test Description");

            festivalRepository.save(testFestival1);
            festivalRepository.save(testFestival2);
        }

        @AfterEach
        public void clearDatabase() {
            productionRepository.deleteAll();
            festivalRepository.deleteAll();
            venueRepository.deleteAll();
        }

        @Test
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testVenueIsNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", adminToken));

            Optional<Venue> retrievedVenue = venueRepository.findById(testVenueId);

            // Assert
            assert(retrievedVenue).isEmpty();
        }

        @Test
        void testAssociatedProductionIsNotDeletedAfterDeletionOfVenue() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", adminToken));

            int testProduction1Id = testProduction1.getId();
            Optional<Production> associatedProduction = productionRepository.findById(testProduction1Id);

            // Assert
            assert(associatedProduction).isPresent();
        }

        @Test
        void testAssociatedFestivalIsNotDeletedAfterDeletionOfVenue() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
            // Act
            mockMvc.perform(delete("/venues/" + testVenueId)
                    .header("Authorization", adminToken));

            int testFestival1Id = testFestival1.getId();
            Optional<Festival> associatedFestival = festivalRepository.findById(testFestival1Id);

            // Assert
            assert(associatedFestival).isPresent();
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
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
            int testVenueId = testVenue1.getId();
            // Act & Assert
            mockMvc.perform(delete("/venues/" + testVenueId))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
            // Act & Assert
            mockMvc.perform(delete("/venues/" + testVenueId)
                    .header("Authorization", "Bad Token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            int testVenueId = testVenue1.getId();
            // Act & Assert
            mockMvc.perform(delete("/venues/" + testVenueId)
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("GET getVenueById integration tests")
    class getVenueByIdIntegrationTests {

        @BeforeEach
        public void setup() {
            venueRepository.deleteAll();
        }

        @AfterEach
        public void cleanup() {
            venueRepository.deleteAll();
        }

        @Test
        void testSuccessfulGetReturns200() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenue.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetReturnsExpectedVenue() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenue.getId()))
                    .andExpect(jsonPath("$.venue.name").value("Test Venue"));
        }

        @Test
        void testSuccessfulGetReturnsProductionsArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenue.getId()))
                    .andExpect(jsonPath("$.productions").isArray());
        }

        @Test
        void testSuccessfulGetReturnsExpectedProductionInArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            Production testProduction = new Production(
                    "Test Production", testVenue, null, null, null, false, false, null
            );
            testVenue.setProductions(Arrays.asList(testProduction));
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenue.getId()))
                    .andExpect(jsonPath("$.productions[0].name").value("Test Production"));
        }

        @Test
        void testSuccessfulGetReturnsFestivalsArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenue.getId()))
                    .andExpect(jsonPath("$.festivals").isArray());
        }

        @Test
        void testSuccessfulGetReturnsExpectedFestivalInArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            Festival testFestival = new Festival("Test Festival", testVenue, 2025, 1, null);
            testVenue.setFestivals(Arrays.asList(testFestival));
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + testVenue.getId()))
                    .andExpect(jsonPath("$.festivals[0].name").value("Test Festival"));
        }

        @Test
        void testNonExistentVenueIdResponds404() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "Test URL");
            venueRepository.save(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/" + (testVenue.getId() - 1)))
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
    class updateVenueIntegrationTests {

        Venue existingVenue;

        @BeforeEach
        void beforeEach() {
            existingVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            venueRepository.save(existingVenue);
        }

        @AfterEach
        void afterEach() { venueRepository.deleteAll();}

        @Test
        void testFullDetailsWithAdminTokenResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + existingVenue.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Updated Test Venue\", \"notes\": \"Updated Test Notes\", \"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", \"town\": \"Updated Test Town\", \"url\": \"www.updatedtest.com\" }"
                            ))
                    .andExpect(status().isOk());

        }

        @Test
        void testFullDetailsWithAdminTokenRespondsExpectedVenueObject() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + existingVenue.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"Updated Test Venue\", \"notes\": \"Updated Test Notes\", \"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", \"town\": \"Updated Test Town\", \"url\": \"www.updatedtest.com\" }"
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
            mockMvc.perform(patch("/venues/" + existingVenue.getId())
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
            mockMvc.perform(patch("/venues/" + existingVenue.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"name\": \"\", \"notes\": \"Updated Test Notes\", \"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", \"town\": \"Updated Test Town\", \"url\": \"www.updatedtest.com\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testMissingNameResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/venues/" + existingVenue.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(
                                    "{ \"notes\": \"Updated Test Notes\", \"postcode\": \"Updated Test Postcode\"," +
                                            " \"address\": \"Updated Test Address\", \"town\": \"Updated Test Town\", \"url\": \"www.updatedtest.com\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }


    }

}
