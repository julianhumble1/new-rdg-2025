package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import com.rdg.rdg_2025.rdg_2025_spring.services.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @InjectMocks
    private PersonController personController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setupContext() {mockMvc = MockMvcBuilders.webAppContextSetup(context).build();}

    private Person testPerson;

    private ObjectNode requestJson;

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
    }

    @Nested
    @DisplayName("addNewPerson controller tests")
    class AddNewPersonControllerTests {

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
        @WithMockUser(roles="ADMIN")
        void testFirstNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("firstName", "");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testFirstNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("firstName");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testLastNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("lastName", "");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testLastNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("lastName");
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testOnlyFirstNameLastNamePresentResponds201() throws Exception {
            // Arrange
            requestJson.remove("summary");
            requestJson.remove("homePhone");
            requestJson.remove("mobilePhone");
            requestJson.remove("addressStreet");
            requestJson.remove("addressTown");
            requestJson.remove("addressPostcode");

            when(personService.addNewPerson(any(PersonRequest.class))).thenReturn(testPerson);
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testAddNewPersonServiceMethodIsCalled() throws Exception {
            // Arrange
            when(personService.addNewPerson(any(PersonRequest.class))).thenReturn(testPerson);
            // Act
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)));
            // Assert
            verify(personService, times(1)).addNewPerson(any(PersonRequest.class));
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDataIntegrityViolationExceptionResponds409() throws Exception {
            // Arrange
            when(personService.addNewPerson(any(PersonRequest.class)))
                    .thenThrow(new DataIntegrityViolationException("data integrity violation"));
            // Act & Assert
            mockMvc.perform(post("/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(personService.addNewPerson(any(PersonRequest.class)))
                    .thenThrow(new DatabaseException("Database exception"));
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testSuccessfulAddResponds201() throws Exception {
            // Arrange
            when(personService.addNewPerson(any(PersonRequest.class))).thenReturn(testPerson);
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testSuccessfulAddRespondsExpectedURI() throws Exception {
            // Arrange
            when(personService.addNewPerson(any(PersonRequest.class))).thenReturn(testPerson);
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(header().string("Location", "/people/" + testPerson.getId()));
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testSuccessfulAddRespondsExpectedPersonJSON() throws Exception {
            // Arrange
            when(personService.addNewPerson(any(PersonRequest.class))).thenReturn(testPerson);
            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.person.firstName").value("Test First Name"))
                    .andExpect(jsonPath("$.person.lastName").value("Test Last Name"));
        }
    }

    @Nested
    @DisplayName("getAllPeople controller tests")
    class GetAllPeopleControllerTests {

        private Person testPerson2;

        private List<Person> personList;

        @BeforeEach
        void setup() {
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

            personList = new ArrayList<>();
            personList.add(testPerson);
            personList.add(testPerson2);
        }

        @Test
        void testGetAllPeopleServiceMethodIsCalled() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(get("/people"));
            // Assert
            verify(personService, times(1)).getAllPeople();

        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(personService.getAllPeople()).thenThrow(new DatabaseException("database exception"));
            // Act & Assert
            mockMvc.perform(get("/people"))
                    .andExpect(status().isInternalServerError());

        }

        @Test
        void testAdminRequestResponds200WhenSuccessful() throws Exception {
            // Arrange
            when(jwtUtils.checkAdmin()).thenReturn(true);
            when(personService.getAllPeople()).thenReturn(personList);
            // Act & Assert
            mockMvc.perform(get("/people"))
                    .andExpect(status().isOk());

        }

        @Test
        void testAdminRequestRespondsExpectedJSONPersonList() throws Exception {
            // Arrange
            when(jwtUtils.checkAdmin()).thenReturn(true);
            when(personService.getAllPeople()).thenReturn(personList);
            // Act & Assert
            mockMvc.perform(get("/people"))
                    .andExpect(jsonPath("$.people[0].firstName").value("Test First Name"))
                    .andExpect(jsonPath("$.people[0].lastName").value("Test Last Name"))
                    .andExpect(jsonPath("$.people[0].summary").value("Test Summary"))
                    .andExpect(jsonPath("$.people[0].mobilePhone").value("07111 111111"))
                    .andExpect(jsonPath("$.people[1].firstName").value("Test First Name 2"))
                    .andExpect(jsonPath("$.people[1].lastName").value("Test Last Name 2"))
                    .andExpect(jsonPath("$.people[1].summary").value("Test Summary"))
                    .andExpect(jsonPath("$.people[1].mobilePhone").value("07111 111111"));

        }

        @Test
        void testNonAdminRequestRespondsRestrictedJSONPersonList() throws Exception {
            // Arrange
            when(jwtUtils.checkAdmin()).thenReturn(false);
            when(personService.getAllPeople()).thenReturn(personList);
            // Act & Assert
            mockMvc.perform(get("/people"))
                    .andExpect(jsonPath("$.people[0].firstName").isNotEmpty())
                    .andExpect(jsonPath("$.people[0].lastName").isNotEmpty())
                    .andExpect(jsonPath("$.people[0].summary").isNotEmpty())
                    .andExpect(jsonPath("$.people[0].homePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].mobilePhone").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressStreet").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressTown").isEmpty())
                    .andExpect(jsonPath("$.people[0].addressPostcode").isEmpty());

        }

    }

    @Nested
    @DisplayName("deletePersonById controller tests")
    class DeletePersonByIdControllerTests {

        @Test
        void testIdNotIntResponds404() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/people/notanint"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testDeletePersonServiceMethodIsCalled() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(delete("/people/1"));
            // Assert
            verify(personService, times(1)).deletePersonById(1);
        }

        @Test
        void testEntityNotFoundExceptionResponds404() throws Exception {
            // Arrange
            doThrow(new EntityNotFoundException("person not found")).when(personService).deletePersonById(anyInt());
            // Act & Assert
            mockMvc.perform(delete("/people/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            doThrow(new DatabaseException("database exception")).when(personService).deletePersonById(anyInt());
            // Act & Assert
            mockMvc.perform(delete("/people/1"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void testNoServiceErrorResponds204() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/people/1"))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    @DisplayName("getPersonById controller tests")
    class GetPersonByIdControllerTests {

        @Test
        void testIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/people/notanint"))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void testGetPersonServiceMethodIsCalled() throws Exception {
            // Arrange
            when(personService.getPersonById(anyInt())).thenReturn(testPerson);
            // Act
            mockMvc.perform(get("/people/1"));
            // Assert
            verify(personService, times(1)).getPersonById(1);

        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(personService.getPersonById(anyInt())).thenThrow(new DatabaseException("database exception"));
            // Act & Assert
            mockMvc.perform(get("/people/1"))
                    .andExpect(status().isInternalServerError());

        }

        @Test
        void testEntityNotFoundExceptionResponds404() throws Exception {
            // Arrange
            when(personService.getPersonById(anyInt())).thenThrow(new EntityNotFoundException("no person with this id"));
            // Act & Assert
            mockMvc.perform(get("/people/1"))
                    .andExpect(status().isNotFound());

        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Arrange
            when(jwtUtils.checkAdmin()).thenReturn(true);
            when(personService.getPersonById(anyInt())).thenReturn(testPerson);
            // Act & Assert
            mockMvc.perform(get("/people/1"))
                    .andExpect(status().isOk());

        }

        @Test
        void testSuccessfulAdminGetRespondsFullPersonDetails() throws Exception {
            // Arrange
            when(jwtUtils.checkAdmin()).thenReturn(true);
            when(personService.getPersonById(anyInt())).thenReturn(testPerson);
            // Act & Assert
            mockMvc.perform(get("/people/1"))
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
        void testSuccessfulNonAdminGetRespondsPublicPersonDetails() throws Exception {
            // Arrange
            when(jwtUtils.checkAdmin()).thenReturn(false);
            when(personService.getPersonById(anyInt())).thenReturn(testPerson);
            // Act & Assert
            mockMvc.perform(get("/people/1"))
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
    @DisplayName("updatePerson controller tests")
    class UpdatePersonControllerTests {

        @BeforeEach
        void setup() {
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
        void testPersonIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/people/notanint")
                    .contentType(MediaType.APPLICATION_JSON)
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
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testLastNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("lastName");
            // Act & Assert
            mockMvc.perform(patch("/people/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testOnlyFirstLastNameNotEmptyResponds200() throws Exception {
            // Arrange
            requestJson.put("summary", "");
            requestJson.put("homePhone", "");
            requestJson.put("mobilePhone" ,"");
            requestJson.put("addressStreet", "");
            requestJson.put("addressTown", "");
            requestJson.put("addressPostcode", "");
            // Act & Assert
            mockMvc.perform(patch("/people/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

    }
}
