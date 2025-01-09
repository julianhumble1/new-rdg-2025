package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.ERole;
import com.rdg.rdg_2025.rdg_2025_spring.models.Role;
import com.rdg.rdg_2025.rdg_2025_spring.models.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
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


import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PostVenueIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

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
        mockMvc.perform(post("/venues/new")
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
        mockMvc.perform(post("/venues/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminToken)
                        .content(
                                "{ \"name\": \"Test Venue\", \"notes\": \"\", \"postcode\": \"\", \"address\": \"\", " +
                                        "\"town\": \"\", \"url\": \"\" }"
                        ))
                .andExpect(status().isCreated());
    }

    @Test
    void testVenueNameOthersMissingWithAdminTokenReturns201() throws Exception {
        mockMvc.perform(post("/venues/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminToken)
                        .content(
                                "{ \"name\": \"Test Venue\" }"
                        ))
                .andExpect(status().isCreated());
    }

    @Test
    void testMissingVenueNameWithAdminTokenReturns400() throws Exception {
        mockMvc.perform(post("/venues/new")
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
        mockMvc.perform(post("/venues/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminToken)
                        .content(
                                "{ \"name\": \"\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                        "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                        ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingTokenReturns401() throws Exception {
        mockMvc.perform(post("/venues/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                        "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                        ))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testBadTokenReturns401() throws Exception {
        mockMvc.perform(post("/venues/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "fake token")
                        .content(
                                "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                        "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                        ))
                .andExpect(status().isUnauthorized());
    }
}
