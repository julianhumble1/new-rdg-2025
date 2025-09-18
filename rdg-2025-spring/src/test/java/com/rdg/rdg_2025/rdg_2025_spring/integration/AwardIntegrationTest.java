package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.auth.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.AwardRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import com.rdg.rdg_2025.rdg_2025_spring.utils.AuthTestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AwardIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AwardRepository awardRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ProductionRepository productionRepository;

    private static User testAdmin;
    private static String adminToken;

    private static User testUser;
    private static String userToken;

    @BeforeAll
    static void setupUsersAndTokens(@Autowired UserRepository userRepository,
            @Autowired RoleRepository roleRepository,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired AuthenticationManager authenticationManager,
            @Autowired JwtUtils jwtUtils) {
        userRepository.deleteAll();

        testAdmin = AuthTestUtils.createTestAdmin(userRepository, roleRepository, passwordEncoder);
        Authentication adminAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test_admin", "password123"));
        adminToken = "Bearer " + jwtUtils.generateJwtToken(adminAuthentication);

        testUser = AuthTestUtils.createTestUser(userRepository, roleRepository, passwordEncoder);
        Authentication userAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test_user", "password123"));
        userToken = "Bearer " + jwtUtils.generateJwtToken(userAuthentication);
    }

    @AfterAll
    static void cleanup(@Autowired UserRepository userRepository,
            @Autowired FestivalRepository festivalRepository,
            @Autowired AwardRepository awardRepository) {
        awardRepository.deleteAll();
        festivalRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Festival makeFestival(String name) {
        Festival f = new Festival();
        f.setName(name);
        f.setYear(2025);
        f.setCreatedAt(LocalDateTime.now());
        return festivalRepository.save(f);
    }

    private Person makePerson(String firstName) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName("LastName");
        p.setSlug(firstName);
        p.setCreatedAt(LocalDateTime.now());
        return personRepository.save(p);
    }

    private Production makeProduction(String name) {
        Production pr = new Production();
        pr.setName(name);
        pr.setCreatedAt(LocalDateTime.now());
        pr.setSlug(name);
        return productionRepository.save(pr);
    }

    @Nested
    @DisplayName("POST /awards - create award")
    class CreateAward {

        @Test
        void adminCanCreate_withAllRefs_returnsCreated() throws Exception {
            Festival fest = makeFestival("Spring Fest");
            Person person = makePerson("Ada");
            Production prod = makeProduction("Hamlet");

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Best Design");
            req.put("festivalId", fest.getId());
            req.put("personId", person.getId());
            req.put("productionId", prod.getId());

            mockMvc.perform(post("/awards")
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/awards/")))
                    .andExpect(content().contentTypeCompatibleWith("application/json"))
                    .andExpect(jsonPath("$.award.name").value("Best Design"));
        }

        @Test
        void adminCanCreate_withoutPerson_returnsCreated() throws Exception {
            Festival fest = makeFestival("Spring Fest");
            Production prod = makeProduction("Hamlet");

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Best Sound");
            req.put("festivalId", fest.getId());
            req.put("personId", 0); // optional per service
            req.put("productionId", prod.getId());

            mockMvc.perform(post("/awards")
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/awards/")))
                    .andExpect(jsonPath("$.award.name").value("Best Sound"));
        }

        @Test
        void createMissingFestival_returnsNotFound() throws Exception {
            Person person = makePerson("Toni");
            Production prod = makeProduction("Macbeth");

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Best Lighting");
            req.put("festivalId", 999999);
            req.put("personId", person.getId());
            req.put("productionId", prod.getId());

            mockMvc.perform(post("/awards")
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void createMissingProduction_returnsNotFound() throws Exception {
            Festival fest = makeFestival("Winter Fest");

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Best Costume");
            req.put("festivalId", fest.getId());
            req.put("personId", 0);
            req.put("productionId", 987654); // invalid

            mockMvc.perform(post("/awards")
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void nonAdminCannotCreate_returnsForbidden() throws Exception {
            Festival fest = makeFestival("Test Fest");
            Production prod = makeProduction("Show");

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Best Design");
            req.put("festivalId", fest.getId());
            req.put("personId", 0);
            req.put("productionId", prod.getId());

            mockMvc.perform(post("/awards")
                    .header("Authorization", userToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void unauthenticatedCreate_returnsUnauthorized() throws Exception {
            Festival fest = makeFestival("Test Fest");
            Production prod = makeProduction("Show");

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Best Design");
            req.put("festivalId", fest.getId());
            req.put("personId", 0);
            req.put("productionId", prod.getId());

            mockMvc.perform(post("/awards")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("GET /awards/{id} - fetch award")
    class GetAwardById {

        @Test
        void anyoneCanGetAwardById_returnsOk() throws Exception {
            Festival fest = makeFestival("Public Fest");
            Person person = makePerson("Sam");
            Production prod = makeProduction("King Lear");

            Award award = new Award();
            award.setName("Outstanding Achievement");
            award.setFestival(fest);
            award.setPerson(person);
            award.setProduction(prod);
            award = awardRepository.save(award);

            mockMvc.perform(get("/awards/" + award.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith("application/json"))
                    .andExpect(jsonPath("$.award.id").value(award.getId()))
                    .andExpect(jsonPath("$.award.name").value("Outstanding Achievement"));
        }

        @Test
        void getNonExistentAward_returnsNotFound() throws Exception {
            mockMvc.perform(get("/awards/999999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PATCH /awards/{id} - update award")
    class UpdateAward {

        @Test
        void adminCanUpdateAward_returnsOk() throws Exception {
            Festival fest = makeFestival("Fest A");
            Person person = makePerson("Rae");
            Production prod = makeProduction("Othello");

            Award award = new Award();
            award.setName("Old Name");
            award.setFestival(fest);
            award.setPerson(person);
            award.setProduction(prod);
            award = awardRepository.save(award);

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "New Name");
            req.put("festivalId", fest.getId());
            req.put("personId", person.getId());
            req.put("productionId", prod.getId());

            mockMvc.perform(patch("/awards/" + award.getId())
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith("application/json"))
                    .andExpect(jsonPath("$.award.id").value(award.getId()))
                    .andExpect(jsonPath("$.award.name").value("New Name"));
        }

        @Test
        void adminCanUpdate_clearPerson_returnsOk() throws Exception {
            Festival fest = makeFestival("Fest B");
            Person person = makePerson("Lee");
            Production prod = makeProduction("Tempest");

            Award award = new Award();
            award.setName("Has Person");
            award.setFestival(fest);
            award.setPerson(person);
            award.setProduction(prod);
            award = awardRepository.save(award);

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Has No Person");
            req.put("festivalId", fest.getId());
            req.put("personId", 0); // clear optional relation
            req.put("productionId", prod.getId());

            mockMvc.perform(patch("/awards/" + award.getId())
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.award.name").value("Has No Person"));
        }

        @Test
        void updateWithInvalidFestival_returnsNotFound() throws Exception {
            Festival fest = makeFestival("Fest C");
            Production prod = makeProduction("Coriolanus");

            Award award = new Award();
            award.setName("Test");
            award.setFestival(fest);
            award.setProduction(prod);
            award = awardRepository.save(award);

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Still Test");
            req.put("festivalId", 123456); // invalid
            req.put("personId", 0);
            req.put("productionId", prod.getId());

            mockMvc.perform(patch("/awards/" + award.getId())
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void nonAdminCannotUpdate_returnsForbidden() throws Exception {
            Festival fest = makeFestival("Fest D");
            Production prod = makeProduction("Richard III");

            Award award = new Award();
            award.setName("Some");
            award.setFestival(fest);
            award.setProduction(prod);
            award = awardRepository.save(award);

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Attempted Update");
            req.put("festivalId", fest.getId());
            req.put("personId", 0);
            req.put("productionId", prod.getId());

            mockMvc.perform(patch("/awards/" + award.getId())
                    .header("Authorization", userToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void unauthenticatedUpdate_returnsUnauthorized() throws Exception {
            Festival fest = makeFestival("Fest E");
            Production prod = makeProduction("Measure for Measure");

            Award award = new Award();
            award.setName("Some");
            award.setFestival(fest);
            award.setProduction(prod);
            award = awardRepository.save(award);

            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Attempted Update");
            req.put("festivalId", fest.getId());
            req.put("personId", 0);
            req.put("productionId", prod.getId());

            mockMvc.perform(patch("/awards/" + award.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void updateNonExistentAward_returnsNotFound() throws Exception {
            ObjectNode req = objectMapper.createObjectNode();
            req.put("name", "Doesn't Matter");
            req.put("festivalId", 1);
            req.put("personId", 0);
            req.put("productionId", 1);

            mockMvc.perform(patch("/awards/999999")
                    .header("Authorization", adminToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /awards/{id} - delete award")
    class DeleteAward {

        @Test
        void adminCanDeleteAward_returnsNoContent() throws Exception {
            Festival fest = makeFestival("Fest F");
            Production prod = makeProduction("Twelfth Night");

            Award award = new Award();
            award.setName("To Be Deleted");
            award.setFestival(fest);
            award.setProduction(prod);
            award = awardRepository.save(award);

            mockMvc.perform(delete("/awards/" + award.getId())
                    .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void nonAdminCannotDelete_returnsForbidden() throws Exception {
            Festival fest = makeFestival("Fest G");
            Production prod = makeProduction("Julius Caesar");

            Award award = new Award();
            award.setName("Keep");
            award.setFestival(fest);
            award.setProduction(prod);
            award = awardRepository.save(award);

            mockMvc.perform(delete("/awards/" + award.getId())
                    .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }

        @Test
        void unauthenticatedDelete_returnsUnauthorized() throws Exception {
            Festival fest = makeFestival("Fest H");
            Production prod = makeProduction("Henry V");

            Award award = new Award();
            award.setName("Keep");
            award.setFestival(fest);
            award.setProduction(prod);
            award = awardRepository.save(award);

            mockMvc.perform(delete("/awards/" + award.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteNonExistentAward_returnsNotFound() throws Exception {
            mockMvc.perform(delete("/awards/999999")
                    .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());
        }
    }
}