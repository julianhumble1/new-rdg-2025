package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import com.rdg.rdg_2025.rdg_2025_spring.services.CreditService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        @Test
        void testPersonIdNotIntResponds400() throws Exception {
            // Arrange
            requestJson.put("personId", "not an int");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCreditServiceMethodIsCalled() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            // Act
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)));
            // Assert
            verify(creditService, times(1)).addNewCredit(any());
        }

        @Test
        void testServiceDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenThrow(new DatabaseException(""));
            // Act & Assert
            mockMvc.perform(post("/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isInternalServerError());

        }

        @Test
        void testServiceEntityNotFoundExceptionResponds404() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenThrow(new EntityNotFoundException(""));
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testSuccessfulAddResponds201() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSuccessfulAddRespondsExpectedURI() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(header().string("Location", "/credits/" + testCredit.getId()));
        }

        @Test
        void testSuccessfulAddRespondsExpectedJSON() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.credit.name").value("Test Credit"))
                    .andExpect(jsonPath("$.credit.type").value("ACTOR"));
        }

        @Test
        void testTypeMusicianResponds201() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("type", "MUSICIAN");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testTypeProducerResponds201() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("type", "PRODUCER");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSummaryAndPersonIdEmptyResponds201() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("summary", "");
            requestJson.put("personId", "");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSummaryAndPersonIdMissingResponds201() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.remove("summary");
            requestJson.remove("personId");
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testPersonIdZeroResponds201() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("personId", 0);
            // Act & Assert
            mockMvc.perform(post("/credits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isCreated());
        }


    }

    @Nested
    @DisplayName("getCreditById controller tests")
    class GetCreditByIdControllerTests {

        @Test
        void testCreditIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/credits/notanint"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testServiceMethodIsCalled() throws Exception {
            // Arrange
            when(creditService.getCreditById(1)).thenReturn(testCredit);
            // Act
            mockMvc.perform(get("/credits/1"));
            // Assert
            verify(creditService, times(1)).getCreditById(1);
        }

        @Test
        void testEntityNotFoundExceptionResponds404() throws Exception {
            // Arrange
            when(creditService.getCreditById(1)).thenThrow(new EntityNotFoundException(""));
            // Act & Assert
            mockMvc.perform(get("/credits/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(creditService.getCreditById(1)).thenThrow(new DatabaseException(""));
            // Act & Assert
            mockMvc.perform(get("/credits/1"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Arrange
            when(creditService.getCreditById(1)).thenReturn(testCredit);
            // Act & Assert
            mockMvc.perform(get("/credits/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetRespondsExpectedJson() throws Exception {
            // Arrange
            when(creditService.getCreditById(1)).thenReturn(testCredit);
            // Act & Assert
            mockMvc.perform(get("/credits/1"))
                    .andExpect(jsonPath("$.credit.name").value("Test Credit"));
        }

    }

    @Nested
    @DisplayName("updateCredit controller tests")
    class UpdateCreditControllerTests {

        @BeforeEach
        void setup() {
            requestJson = objectMapper.createObjectNode();
            requestJson.put("name", "Updated Test Credit");
            requestJson.put("type", "MUSICIAN");
            requestJson.put("productionId", 2);
            requestJson.put("personId", 2);
            requestJson.put("summary", "Updated Test Summary");
        }


        @Test
        void testCreditIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/credits/notanint")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNameEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("name", "");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testNameMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("name");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("type", "");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("type");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testTypeNotAppropriateResponds400() throws Exception {
            // Arrange
            requestJson.put("type", "not actor musician or producer");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdEmptyResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", "");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdMissingResponds400() throws Exception {
            // Arrange
            requestJson.remove("productionId");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdNotIntResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", "not an int");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testProductionIdZeroResponds400() throws Exception {
            // Arrange
            requestJson.put("productionId", 0);
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testPersonIdNotIntResponds400() throws Exception {
            // Arrange
            requestJson.put("personId", "not an int");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCreditServiceMethodIsCalled() throws Exception {
            // Arrange
            // Act
            mockMvc.perform(patch("/credits/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestJson)));
            // Assert
            verify(creditService, times(1)).updateCredit(anyInt(), any());

        }

        @Test
        void testServiceDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(creditService.updateCredit(anyInt(), any())).thenThrow(new DatabaseException(""));
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isInternalServerError());

        }

        @Test
        void testServiceEntityNotFoundExceptionResponds404() throws Exception {
            // Arrange
            when(creditService.updateCredit(anyInt(), any())).thenThrow(new EntityNotFoundException(""));
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isNotFound());

        }

        @Test
        void testSuccessfulUpdateResponds200() throws Exception {
            // Arrange
            when(creditService.updateCredit(anyInt(), any())).thenReturn(testCredit);
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());

        }

        @Test
        void testSuccessfulUpdateRespondsExpectedJSON() throws Exception {
            // Arrange
            when(creditService.updateCredit(anyInt(), any())).thenReturn(testCredit);
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(jsonPath("$.credit.name").value("Test Credit"))
                    .andExpect(jsonPath("$.credit.type").value("ACTOR"));
        }

        @Test
        void testTypeMusicianResponds200() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("type", "MUSICIAN");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

        @Test
        void testTypeProducerResponds200() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("type", "PRODUCER");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

        @Test
        void testSummaryAndPersonIdEmptyResponds200() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("summary", "");
            requestJson.put("personId", "");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

        @Test
        void testSummaryAndPersonIdMissingResponds200() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.remove("summary");
            requestJson.remove("personId");
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

        @Test
        void testPersonIdZeroResponds200() throws Exception {
            // Arrange
            when(creditService.addNewCredit(any())).thenReturn(testCredit);
            requestJson.put("personId", 0);
            // Act & Assert
            mockMvc.perform(patch("/credits/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestJson)))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    @DisplayName("deleteCreditById controller tests")
    class DeleteCreditByIdControllerTests {

        @Test
        void testCreditIdNotIntResponds400() throws Exception{
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/credits/notanint"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCreditServiceMethodIsCalled() throws Exception{
            // Arrange
            // Act
            mockMvc.perform(delete("/credits/1"));
            // Assert
            verify(creditService, times(1)).deleteCreditById(1);
        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception{
            // Arrange
            doThrow(new DatabaseException("")).when(creditService).deleteCreditById(anyInt());
            // Act & Assert
            mockMvc.perform(delete("/credits/1"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void testEntityNotFoundExceptionResponds404() throws Exception{
            // Arrange
            doThrow(new EntityNotFoundException("")).when(creditService).deleteCreditById(anyInt());
            // Act & Assert
            mockMvc.perform(delete("/credits/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testSuccessfulDeleteResponds204() throws Exception{
            // Arrange
            Production production = new Production();
            // Act & Assert
            mockMvc.perform(delete("/credits/1"))
                    .andExpect(status().isNoContent());
        }


    }

}
