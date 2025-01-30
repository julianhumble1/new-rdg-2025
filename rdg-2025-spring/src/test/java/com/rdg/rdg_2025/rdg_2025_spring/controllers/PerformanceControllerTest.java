package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.services.PerformanceService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerformanceController.class)
public class PerformanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PerformanceService performanceService;

    @InjectMocks
    PerformanceController performanceController;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setupContext() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private Performance testPerformance;

    @BeforeEach
    public void setup() {
        testPerformance = new Performance(
                new Production(),
                new Venue(),
                new Festival(),
                LocalDateTime.MAX,
                "Test Performance Description",
                new BigDecimal("10.00"),
                new BigDecimal("9.00"),
                "Test Box Office"
        );
    }

    @Nested
    @DisplayName("addNewPerformance controller tests")
    class AddNewPerformanceControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void testProductionIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        "{\"productionId\": " + "\"not an int\"" + ", \"venueId\": 1, \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                    ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testProductionIdBlankResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": " + "\"\"" + ", \"venueId\": 1, \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testProductionIdMissingResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"venueId\": 1, \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testVenueIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": " + "\"not an int\"" + ", \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testVenueIdBlankResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": " + "\"\"" + ", \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testVenueIdMissingResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testFestivalIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": 1, \"festivalId\": " + "\"not an int\"" + ", \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testTimeNotDateTimeResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": 1, \"festivalId\": 1, \"time\": \"not a date time\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testTimeEmptyResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": 1, \"festivalId\": 1, \"time\": \"\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testTimeMissingResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": 1, \"festivalId\": 1, \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testStandardPriceNotNumberResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": 1, \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"not a number\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testConcessionPriceNotNumberResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": 1, \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"not a number\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testServiceDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(performanceService.addNewPerformance(any())).thenThrow(new DatabaseException("Database exception"));
            // Act & Assert
            mockMvc.perform(post("/performances")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"productionId\": 1, \"venueId\": 1, \"festivalId\": 1, \"time\": \"2025-10-10T10:00:00\", \"description\": \"Test Performance Description\"" +
                                            ", \"standardPrice\": \"10.00\", \"concessionPrice\": \"9.00\", \"boxOffice\": \"Test Box Office\" }"
                            ))
                    .andExpect(status().isInternalServerError());

        }


    }


}
