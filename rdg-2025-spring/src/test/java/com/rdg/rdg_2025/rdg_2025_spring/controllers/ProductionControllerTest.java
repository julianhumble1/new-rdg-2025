package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.ProductionService;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductionController.class)
public class ProductionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ProductionService productionService;

    @InjectMocks
    private ProductionController productionController;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Nested
    @DisplayName("addNewProduction controller tests")
    class addNewProductionControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void test201StatusWhenServiceSuccessfullySavesProduction() throws Exception {
            // Arrange
            Production testProduction = new Production(
                    "Test Production",
                    new Venue(),
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test File String"
            );

            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenReturn(testProduction);

            // Act & Assert
            mockMvc.perform(post("/productions/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{ \"name\": \"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                    "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                    ))
                    .andExpect(status().isCreated()
                    );

        }



    }

}
