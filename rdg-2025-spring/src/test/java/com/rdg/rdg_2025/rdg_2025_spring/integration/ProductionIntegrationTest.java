package com.rdg.rdg_2025.rdg_2025_spring.integration;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    public void setup() {productionRepository.deleteAll();}

    @AfterEach
    public void cleanup() {productionRepository.deleteAll();}

    @Test
    void testFullProductionDetailsWithNoVenueReturns201() throws Exception {
        mockMvc.perform(post("/productions/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminToken)
                        .content(
                                "{ \"name\": \"Test Production\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                        "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                        ))
                .andExpect(status().isCreated()
                );
    }

    @Test
    void testFullProductionDetailsWithNoVenueReturnsExpectedProductionObject() throws Exception {
        mockMvc.perform(post("/productions/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminToken)
                        .content(
                                "{ \"name\": \"Test Production\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                        "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                        ))
                .andExpect(jsonPath("$.production.name").value("Test Production")
                );
    }

    @Test
    void testFullProductionDetailsWithValidVenueReturns201() throws Exception {
        mockMvc.perform(post("/productions/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminToken)
                        .content(
                                "{ \"name\": \"Test Production\", \"venueId\": " + testVenue1.getId() + ", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                        "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                        ))
                .andExpect(status().isCreated()
                );
    }

    @Test
    void testOnlyNameReturns201() throws Exception {
        mockMvc.perform(post("/productions/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminToken)
                        .content(
                                "{ \"name\": \"Test Production\"}"
                        ))
                .andExpect(status().isCreated()
                );
    }


}
