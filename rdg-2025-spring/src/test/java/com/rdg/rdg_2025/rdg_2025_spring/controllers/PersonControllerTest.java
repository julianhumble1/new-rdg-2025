package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    ObjectMapper objectMapper;

    @BeforeEach
    public void setupContext() {mockMvc = MockMvcBuilders.webAppContextSetup(context).build();}

    private Person testPerson;

    private PersonRequest testPersonRequest;

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

        testPersonRequest = new PersonRequest(
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

        @Test
        @WithMockUser(roles="ADMIN")
        void testFirstNameMissingResponds400() throws Exception {
            // Arrange
            testPersonRequest.setFirstName(null);

            // Act & Assert
            mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPersonRequest)))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testFirstNameEmptyResponds400() throws Exception {
            // Arrange
            testPersonRequest.setFirstName("");

            // Act & Assert
            mockMvc.perform(post("/people")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testPersonRequest)))
                    .andExpect(status().isBadRequest());

        }
    }
}
