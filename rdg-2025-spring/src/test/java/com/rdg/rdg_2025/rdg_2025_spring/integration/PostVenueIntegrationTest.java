package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.rdg.rdg_2025.rdg_2025_spring.models.ERole;
import com.rdg.rdg_2025.rdg_2025_spring.models.Role;
import com.rdg.rdg_2025.rdg_2025_spring.models.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private User testAdmin;
    private String adminToken;

    private User testUser;
    private String userToken;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        venueRepository.deleteAll();

        testAdmin = new User();
        testAdmin.setUsername("test_admin");
        testAdmin.setEmail("admin@test.com");
        testAdmin.setPassword(passwordEncoder.encode("password123"));
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN) .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
        Set<Role> testAdminRoles = new HashSet<>();
        testAdminRoles.add(adminRole);
        testAdmin.setRoles(testAdminRoles);
        userRepository.save(testAdmin);
        Authentication adminAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test_admin", "password123")
        );
        adminToken = "Bearer " + jwtUtils.generateJwtToken(adminAuthentication);

        testUser = new User();
        testUser.setUsername("test_user");
        testUser.setEmail("user@test.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        Role userRole = roleRepository.findByName(ERole.ROLE_USER) .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
        Set<Role> testUserRoles = new HashSet<>();
        testUserRoles.add(userRole);
        testUser.setRoles(testUserRoles);
        userRepository.save(testUser);
        Authentication userAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test_user", "password123")
        );
        userToken = "Bearer " + jwtUtils.generateJwtToken(userAuthentication);

    }


    @Test
    void testFullVenueDetailsWithAdminTokenReturns200() throws Exception {
        mockMvc.perform(post("/venues/new")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminToken)
                .content(
                        "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                ))
                .andExpect(status().isOk());
    }
}
