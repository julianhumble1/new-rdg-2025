package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals.NewFestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.FestivalService;
import jakarta.persistence.EntityNotFoundException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FestivalController.class)
public class FestivalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FestivalService festivalService;

    @InjectMocks
    FestivalController festivalController;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    private Festival testFestival = new Festival(
            "Test Festival", new Venue(), 2025, 1, "Test Description"
    );


    @Nested
    @DisplayName("addNewFestival controller tests")
    class addNewFestivalControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void test201StatusWhenServiceSuccessfullySavesFestival() throws Exception {
            // Arrange
            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenReturn(testFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{\"name\": \"Test Festival\", \"venueId\": 1, \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                    ))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testExpectedURIWhenServiceSuccessfullySavesFestival() throws Exception {
            // Arrange
            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenReturn(testFestival);
            int testFestivalId = testFestival.getId();

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"Test Festival\", \"venueId\": 1, \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(header().string("Location", "/festivals" + "/" + testFestivalId));
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testExpectedReturnedFestivalWhenServiceSuccessfullySavesFestival() throws Exception{
            // Arrange
            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenReturn(testFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"Test Festival\", \"venueId\": 1, \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(jsonPath("$.festival.id").isNotEmpty())
                    .andExpect(jsonPath("$.festival.name").value(testFestival.getName()))
                    .andExpect(jsonPath("$.festival.venue").isNotEmpty())
                    .andExpect(jsonPath("$.festival.year").value(testFestival.getYear()))
                    .andExpect(jsonPath("$.festival.month").value(testFestival.getMonth()))
                    .andExpect(jsonPath("$.festival.description").value(testFestival.getDescription()))
                    .andExpect(jsonPath("$.festival.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.festival.updatedAt").isNotEmpty()


                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenThrow(new DatabaseException("Database Error"));

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"Test Festival\", \"venueId\": 1, \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testInvalidVenueIdReturns400BadRequest() throws Exception{
            // Arrange
            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenThrow(new EntityNotFoundException("Invalid venue id"));

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"Test Festival\", \"venueId\": 1, \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testOnlyNameAndYearWithEmptyOtherValuesReturns201() throws Exception {
            // Arrange
            Festival onlyNameAndYearFestival = new Festival("Test Festival", null, 2025, 0, null);

            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenReturn(onlyNameAndYearFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"Test Festival\", \"venueId\": \"\", \"year\": 2025, \"month\": \"\", \"description\": \"\"}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testOnlyNameAndYearWithMissingOtherValuesReturns201() throws Exception {
            // Arrange
            Festival onlyNameAndYearFestival = new Festival("Test Festival", null, 2025, 0, null);

            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenReturn(onlyNameAndYearFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"Test Festival\", \"year\": 2025}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testEmptyNameResponds400BadRequest() throws Exception {
            // Arrange
            Festival onlyNameAndYearFestival = new Festival("Test Festival", null, 2025, 0, null);

            when(festivalService.addNewFestival(any(NewFestivalRequest.class))).thenReturn(onlyNameAndYearFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"\", \"venueId\": 1, \"year\": 2025, \"month\": 1, \"description\": \"Test Description\"}"
                            ))
                    .andExpect(status().isBadRequest());
        }



    }

}
