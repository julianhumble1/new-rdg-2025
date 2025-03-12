package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import com.rdg.rdg_2025.rdg_2025_spring.services.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditController.class)
public class CreditControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CreditService creditService;

    @InjectMocks
    CreditController creditController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setupContext() {mockMvc = MockMvcBuilders.webAppContextSetup(context).build();}

    private Credit testCredit;

    private ObjectNode requestJson;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        testCredit = new Credit(
                "Test Credit",
                CreditType.ACTOR,
                new Person(),
                new Production(),
                "Test Summary"
        );
    }

    @Nested
    @DisplayName("addNewCredit controller tests")
    class AddNewCreditControllerTests {

        @BeforeEach
        void setup() {
            requestJson = objectMapper.createObjectNode();
            requestJson.put("name", "Test Credit");
            requestJson.put("type", "ACTOR");
            requestJson.put("productionId", 1);
            requestJson.put("personId", 1);
            requestJson.put("summary", "Test Summary");
        }

        @Test
        void testNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("name", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("name");

            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("type", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("type");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeNotAppropriateResponds400() throws Exception {
            // Arrange
            requestJson.put("type", "not actor musician or producer");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("productionId");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdNotIntResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", "not an int");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdZeroResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", 0);
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }


    }

}
