package com.rdg.rdg_2025.rdg_2025_spring.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.auth.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    private static User testAdmin;
    private static String adminToken;

    private static User testUser;
    private static String userToken;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode requestJson;

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

    @Nested
    @DisplayName("POST add new person integration tests")
    class AddNewPersonIntegrationTests {

        @BeforeEach
        void setup() {
            requestJson = objectMapper.createObjectNode();
            requestJson.put("firstName", "Test First Name");
            requestJson.put("lastName", "Test Last Name");
            requestJson.put("summary", "Test Summary");
            requestJson.put("homePhone", "01111 111111");
            requestJson.put("mobilePhone" ,"07111 111111");
            requestJson.put("addressStreet", "Test Street");
            requestJson.put("addressTown", "Test Town");
            requestJson.put("addressPostcode", "Test Postcode");
        }


        @Test
        void testSuccessfulAddWithAllFieldsResponds201() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());

        }

        @Test
        void testSuccessfulAddWithAllFieldsRespondsExpectedPersonJSON() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.person.firstName").value("Test First Name"))
                    .andExpect(jsonPath("$.person.lastName").value("Test Last Name"))
                    .andExpect(jsonPath("$.person.summary").value("Test Summary"))
                    .andExpect(jsonPath("$.person.homePhone").value("01111 111111"))
                    .andExpect(jsonPath("$.person.mobilePhone").value("07111 111111"))
                    .andExpect(jsonPath("$.person.addressStreet").value("Test Street"))
                    .andExpect(jsonPath("$.person.addressTown").value("Test Town"))
                    .andExpect(jsonPath("$.person.addressPostcode").value("Test Postcode"))
                    .andExpect(jsonPath("$.person.id").isNumber())
                    .andExpect(jsonPath("$.person.slug").value("test-first-name-test-last-name"))
                    .andExpect(jsonPath("$.person.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.person.updatedAt").isNotEmpty());


        }

        @Test
        void testSuccessfulAddAddsRowToDatabase() throws Exception {
            // Arrange
            int startingLength = personRepository.findAll().size();
            // Act
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)));
            // Assert
            int endingLength = personRepository.findAll().size();
            assertEquals(startingLength + 1, endingLength);
        }

        @Test
        void testDuplicateFirstNameAndLastNameResponds409() throws Exception {
            // Arrange
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());

            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isConflict());

        }

        @Test
        void testFirstNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("firstName", "");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testFirstNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("firstName");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testLastNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("lastName", "");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testLastNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("lastName");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testOnlyFirstNameLastNamePresentResponds201() throws Exception {
            // Arrange
            requestJson.remove("summary");
            requestJson.remove("homePhone");
            requestJson.remove("mobilePhone");
            requestJson.remove("addressStreet");
            requestJson.remove("addressTown");
            requestJson.remove("addressPostcode");

            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testOnlyFirstNameLastNamePresentRespondsExpectedJSON() throws Exception {
            // Arrange
            requestJson.remove("summary");
            requestJson.remove("homePhone");
            requestJson.remove("mobilePhone");
            requestJson.remove("addressStreet");
            requestJson.remove("addressTown");
            requestJson.remove("addressPostcode");

            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.person.firstName").value("Test First Name"))
                    .andExpect(jsonPath("$.person.lastName").value("Test Last Name"))
                    .andExpect(jsonPath("$.person.summary").isEmpty())
                    .andExpect(jsonPath("$.person.homePhone").isEmpty())
                    .andExpect(jsonPath("$.person.mobilePhone").isEmpty())
                    .andExpect(jsonPath("$.person.addressStreet").isEmpty())
                    .andExpect(jsonPath("$.person.addressTown").isEmpty())
                    .andExpect(jsonPath("$.person.addressPostcode").isEmpty())
                    .andExpect(jsonPath("$.person.id").isNumber())
                    .andExpect(jsonPath("$.person.slug").value("test-first-name-test-last-name"))
                    .andExpect(jsonPath("$.person.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.person.updatedAt").isNotEmpty());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", userToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isForbidden());

        }

        @Test
        void testUserTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "bad token")
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isUnauthorized());

        }

        @Test
        void testMissingTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isUnauthorized());

        }

    }

    @Nested
    @DisplayName("GET get all people integration tests")
    class GetAllPeopleIntegrationTests {

        private Person testPerson1;
        private Person testPerson2;

        @BeforeEach
        void setup() {
            testPerson1 = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            testPerson2 = new Person(
                    "Test First Name 2",
                    "Test Last Name 2",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );

            personRepository.save(testPerson1);
            personRepository.save(testPerson2);
        }

        @Test
        void testAdminSuccessfulGetResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people")
                            .header("Authorization", adminToken))
                    .andExpect(status().isOk());
        }

        @Test
        void testAdminSuccessfulGetRespondsPeopleArray() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people")
                            .header("Authorization", adminToken))
                    .andExpect(jsonPath("$.people").isArray());
        }

        @Test
        void testAdminSuccessfulGetRespondsFullDetailsOfPeople() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people")
                            .header("Authorization", adminToken))
                    .andExpect(jsonPath("$.people[0].id").value(testPerson1.getId()))
                    .andExpect(jsonPath("$.people[0].firstName").value(testPerson1.getFirstName()))
                    .andExpect(jsonPath("$.people[0].lastName").value(testPerson1.getLastName()))
                    .andExpect(jsonPath("$.people[0].summary").value(testPerson1.getSummary()))
                    .andExpect(jsonPath("$.people[0].homePhone").value(testPerson1.getHomePhone()))
                    .andExpect(jsonPath("$.people[0].mobilePhone").value(testPerson1.getMobilePhone()))
                    .andExpect(jsonPath("$.people[0].addressStreet").value(testPerson1.getAddressStreet()))
                    .andExpect(jsonPath("$.people[0].addressTown").value(testPerson1.getAddressTown()))
                    .andExpect(jsonPath("$.people[0].addressPostcode").value(testPerson1.getAddressPostcode()));
        }

        @Test
        void testUserSuccessfulGetRespondsOnlyPublicDetails() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people")
                            .header("Authorization", userToken))
                    .andExpect(jsonPath("$.people[0].id").value(testPerson1.getId()))
                    .andExpect(jsonPath("$.people[0].firstName").value(testPerson1.getFirstName()))
                    .andExpect(jsonPath("$.people[0].lastName").value(testPerson1.getLastName()))
                    .andExpect(jsonPath("$.people[0].summary").value(testPerson1.getSummary()))
                    .andExpect(jsonPath("$.people[0].homePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].mobilePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressStreet").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressTown").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressPostcode").isEmpty());
        }

        @Test
        void testBadTokenRespondsOnlyPublicDetails() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people")
                            .header("Authorization", "bad token"))
                    .andExpect(jsonPath("$.people[0].id").value(testPerson1.getId()))
                    .andExpect(jsonPath("$.people[0].firstName").value(testPerson1.getFirstName()))
                    .andExpect(jsonPath("$.people[0].lastName").value(testPerson1.getLastName()))
                    .andExpect(jsonPath("$.people[0].summary").value(testPerson1.getSummary()))
                    .andExpect(jsonPath("$.people[0].homePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].mobilePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressStreet").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressTown").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressPostcode").isEmpty());
        }

        @Test
        void testMissingAuthHeaderRespondsOnlyPublicDetails() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people"))
                    .andExpect(jsonPath("$.people[0].id").value(testPerson1.getId()))
                    .andExpect(jsonPath("$.people[0].firstName").value(testPerson1.getFirstName()))
                    .andExpect(jsonPath("$.people[0].lastName").value(testPerson1.getLastName()))
                    .andExpect(jsonPath("$.people[0].summary").value(testPerson1.getSummary()))
                    .andExpect(jsonPath("$.people[0].homePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].mobilePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressStreet").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressTown").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressPostcode").isEmpty());
        }

    }

    @Nested
    @DisplayName("DELETE delete person by id integration tests")
    class DeletePersonByIdIntegrationTests {

        private Person testPerson;
        private int testPersonId;

        @BeforeEach
        void setup() {
            testPerson = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            personRepository.save(testPerson);
            testPersonId = testPerson.getId();
        }

        @Test
        void testSuccessfulDeleteResponds204() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/people/" + testPersonId)
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testNonExistentPersonIdResponds404() throws Exception {
            // Arrange
            assertFalse(personRepository.existsById(testPersonId + 1));
            // Act & Assert
            mockMvc.perform(delete("/people/" + (testPersonId + 1))
                            .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testPersonIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/people/notanint" )
                            .header("Authorization", adminToken))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testUserTokenResponds403() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/people/" + testPersonId)
                            .header("Authorization", userToken))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testBadTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/people/" + testPersonId)
                            .header("Authorization", "bad token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testNoTokenResponds401() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/people/" + testPersonId))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testPersonNoLongerInDatabaseFollowingDeletion() throws Exception {
            // Arrange
            assertTrue(personRepository.existsById(testPersonId));
            // Act
            mockMvc.perform(delete("/people/" + testPersonId)
                            .header("Authorization", adminToken))
                    .andExpect(status().isNoContent());
            // Assert
            assertFalse(personRepository.existsById(testPersonId));
        }




    }

    @Nested
    @DisplayName("GET get person by id integration tests")
    class GetPersonByIdIntegrationTests {

        private Person testPerson;
        private int testPersonId;

        @BeforeEach
        void setup() {
            testPerson = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            personRepository.save(testPerson);
            testPersonId = testPerson.getId();
        }

        @Test
        void testPersonIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people/notanint"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testInvalidPersonIdResponds404() throws Exception {
            // Arrange
            assertFalse(personRepository.existsById(testPersonId + 1));
            // Act & Assert
            mockMvc.perform(get("/people/" + (testPersonId + 1)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people/" + testPersonId))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulAdminGetRespondsDetailedPerson() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people/" + testPersonId)
                        .header("Authorization", adminToken))
                    .andExpect(jsonPath("$.responseType").value("DETAILED"))
                    .andExpect(jsonPath("$.person.firstName").value(testPerson.getFirstName()))
                    .andExpect(jsonPath("$.person.lastName").value(testPerson.getLastName()))
                    .andExpect(jsonPath("$.person.summary").value(testPerson.getSummary()))
                    .andExpect(jsonPath("$.person.homePhone").value(testPerson.getHomePhone()))
                    .andExpect(jsonPath("$.person.mobilePhone").value(testPerson.getMobilePhone()))
                    .andExpect(jsonPath("$.person.addressStreet").value(testPerson.getAddressStreet()))
                    .andExpect(jsonPath("$.person.addressTown").value(testPerson.getAddressTown()))
                    .andExpect(jsonPath("$.person.addressPostcode").value(testPerson.getAddressPostcode()));
        }

        @Test
        void testSuccessfulUserGetRespondsPublicPerson() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people/" + testPersonId)
                            .header("Authorization", userToken))
                    .andExpect(jsonPath("$.responseType").value("PUBLIC"))
                    .andExpect(jsonPath("$.person.firstName").value(testPerson.getFirstName()))
                    .andExpect(jsonPath("$.person.lastName").value(testPerson.getLastName()))
                    .andExpect(jsonPath("$.person.summary").value(testPerson.getSummary()))
                    .andExpect(jsonPath("$.person.homePhone").value(""))
                    .andExpect(jsonPath("$.person.mobilePhone").value(""))
                    .andExpect(jsonPath("$.person.addressStreet").value(""))
                    .andExpect(jsonPath("$.person.addressTown").value(""))
                    .andExpect(jsonPath("$.person.addressPostcode").value(""));
        }

        @Test
        void testNoTokenProvidedRespondsPublicPerson() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people/" + testPersonId))
                    .andExpect(jsonPath("$.responseType").value("PUBLIC"))
                    .andExpect(jsonPath("$.person.firstName").value(testPerson.getFirstName()))
                    .andExpect(jsonPath("$.person.lastName").value(testPerson.getLastName()))
                    .andExpect(jsonPath("$.person.summary").value(testPerson.getSummary()))
                    .andExpect(jsonPath("$.person.homePhone").value(""))
                    .andExpect(jsonPath("$.person.mobilePhone").value(""))
                    .andExpect(jsonPath("$.person.addressStreet").value(""))
                    .andExpect(jsonPath("$.person.addressTown").value(""))
                    .andExpect(jsonPath("$.person.addressPostcode").value(""));
        }
    }

    @Nested
    @DisplayName("PATCH update person integration tests")
    class UpdatePersonIntegrationTests {

        private Person testPerson;
        private int testPersonId;


        @BeforeEach
        void setup() {
            testPerson = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            personRepository.save(testPerson);
            testPersonId = testPerson.getId();

            requestJson = objectMapper.createObjectNode();
            requestJson.put("firstName", "Updated First Name");
            requestJson.put("lastName", "Updated Last Name");
            requestJson.put("summary", "Updated Summary");
            requestJson.put("homePhone", "01222 222222");
            requestJson.put("mobilePhone" ,"07222 222222");
            requestJson.put("addressStreet", "Updated Street");
            requestJson.put("addressTown", "Updated Town");
            requestJson.put("addressPostcode", "Updated Postcode");
        }

        @Test
        void testSuccessfulUpdateWithAllFieldsResponds201() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/people/" + testPersonId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulUpdateWithAllFieldsRespondsExpectedJson() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/people/" + testPersonId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.person.firstName").value("Updated First Name"))
                    .andExpect(jsonPath("$.person.lastName").value("Updated Last Name"))
                    .andExpect(jsonPath("$.person.summary").value("Updated Summary"))
                    .andExpect(jsonPath("$.person.homePhone").value("01222 222222"))
                    .andExpect(jsonPath("$.person.mobilePhone").value("07222 222222"))
                    .andExpect(jsonPath("$.person.addressStreet").value("Updated Street"))
                    .andExpect(jsonPath("$.person.addressTown").value("Updated Town"))
                    .andExpect(jsonPath("$.person.addressPostcode").value("Updated Postcode"))
                    .andExpect(jsonPath("$.person.id").isNumber())
                    .andExpect(jsonPath("$.person.slug").value("updated-first-name-updated-last-name"))
                    .andExpect(jsonPath("$.person.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.person.updatedAt").isNotEmpty());
        }

        @Test
        void testSuccessfulUpdateEditsRowInDatabase() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(patch("/people/" + testPersonId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", adminToken)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
            // Assert
            assertTrue(personRepository.findById(testPersonId).get().getFirstName().equals("Updated First Name"));

        }

        @Test
        void testNonExistentPersonIdResponds404() throws Exception {
            // Arrange
            assertFalse(personRepository.existsById(testPersonId + 1));
            // Act & Assert
            mockMvc.perform(patch("/people/" + (testPersonId + 1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testPersonIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/people/notanint")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testFirstNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("firstName", "");
            // Act & Assert
            mockMvc.perform(patch("/people/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testFirstNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("firstName");
            // Act & Assert
            mockMvc.perform(patch("/people/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testLastNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("lastName", "");
            // Act & Assert
            mockMvc.perform(patch("/people/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", adminToken)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }
    }
}
