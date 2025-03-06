package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.PersonService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

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

        @Test
        void testGetAllPeopleServiceMethodIsCalled() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(get("/people"));
            // Assert
            verify(personService, times(1)).getAllPeople();


        }

    }
}
