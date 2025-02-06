package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festival.FestivalRequest;
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

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    class AddNewFestivalControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void test201StatusWhenServiceSuccessfullySavesFestival() throws Exception {
            // Arrange
            when(festivalService.addNewFestival(any(FestivalRequest.class))).thenReturn(testFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{" +
                                    "\"name\": \"Test Festival\"," +
                                    " \"venueId\": 1, " +
                                    "\"year\": 2025, " +
                                    "\"month\": 1, " +
                                    "\"description\": \"Test Description\"" +
                                    "}"
                    ))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testExpectedURIWhenServiceSuccessfullySavesFestival() throws Exception {
            // Arrange
            when(festivalService.addNewFestival(any(FestivalRequest.class))).thenReturn(testFestival);
            int testFestivalId = testFestival.getId();

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\"," +
                                            " \"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(header().string("Location", "/festivals" + "/" + testFestivalId));
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testExpectedReturnedFestivalWhenServiceSuccessfullySavesFestival() throws Exception{
            // Arrange
            when(festivalService.addNewFestival(any(FestivalRequest.class))).thenReturn(testFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(jsonPath("$.festival.id").isNotEmpty())
                    .andExpect(jsonPath("$.festival.name").value(testFestival.getName()))
                    .andExpect(jsonPath("$.festival.venue").isNotEmpty())
                    .andExpect(jsonPath("$.festival.year").value(testFestival.getYear()))
                    .andExpect(jsonPath("$.festival.month").value(testFestival.getMonth()))
                    .andExpect(jsonPath("$.festival.description").value(testFestival.getDescription()))
                    .andExpect(jsonPath("$.festival.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.festival.updatedAt").isNotEmpty());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(festivalService.addNewFestival(any(FestivalRequest.class))).thenThrow(new DatabaseException("Database Error"));

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testInvalidVenueIdReturns400BadRequest() throws Exception{
            // Arrange
            when(festivalService.addNewFestival(any(FestivalRequest.class))).thenThrow(new EntityNotFoundException("Invalid venue id"));

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testOnlyNameAndYearWithEmptyOtherValuesReturns201() throws Exception {
            // Arrange
            Festival onlyNameAndYearFestival = new Festival("Test Festival", null, 2025, 0, null);

            when(festivalService.addNewFestival(any(FestivalRequest.class))).thenReturn(onlyNameAndYearFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": \"\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": \"\", " +
                                            "\"description\": \"\"" +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testOnlyNameAndYearWithMissingOtherValuesReturns201() throws Exception {
            // Arrange
            Festival onlyNameAndYearFestival = new Festival("Test Festival", null, 2025, 0, null);

            when(festivalService.addNewFestival(any(FestivalRequest.class))).thenReturn(onlyNameAndYearFestival);

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"year\": 2025" +
                                            "}"
                            ))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testEmptyNameResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testMissingNameResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testEmptyYearResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": \"\", " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testMissingYearResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testNonIntYearResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": \"Not an int\", " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testNegativeYearResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": -1, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testBadVenueIdResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": \"Bad Venue Id\", " +
                                            "\"year\": 2025, " +
                                            "\"month\": 1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testMonthBelowZeroResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": -1, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testMonthAboveTwelveResponds400BadRequest() throws Exception {
            // Arrange

            // Act & Assert
            mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Test Festival\", " +
                                            "\"venueId\": 1, " +
                                            "\"year\": 2025, " +
                                            "\"month\": 13, " +
                                            "\"description\": \"Test Description\"" +
                                            "}"
                            ))
                    .andExpect(status().isBadRequest());
        }



    }

    @Nested
    @DisplayName("getAllFestivals controller tests")
    class GetAllFestivalsControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void testSuccessfulGetReturns200Code() throws Exception {
            // Arrange
            ArrayList<Festival> festivals = new ArrayList<>();
            when(festivalService.getAllFestivals()).thenReturn(festivals);
            // Act & Assert
            mockMvc.perform(get("/festivals"))
                    .andExpect(status().isOk());


        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testSuccessfulGetContainsFestivalObject() throws Exception {
            // Arrange
            ArrayList<Festival> festivals = new ArrayList<>();
            festivals.add(testFestival);
            when(festivalService.getAllFestivals()).thenReturn(festivals);
            // Act & Assert
            mockMvc.perform(get("/festivals"))
                    .andExpect(jsonPath("$.festivals[0].name").value("Test Festival"));

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(festivalService.getAllFestivals()).thenThrow(new DatabaseException("Database Error"));

            // Act & Assert
            mockMvc.perform(get("/festivals"))
                    .andExpect(status().isInternalServerError());
        }

    }

    @Nested
    @DisplayName("getFestivalById controller tests")
    class GetFestivalByIdControllerTests {

        @Test
        void testEntityNotFoundExceptionResponds404() throws Exception {
            // Arrange
            when(festivalService.getFestivalById(anyInt())).thenThrow(new EntityNotFoundException());
            // Act & Assert
            mockMvc.perform(get("/festivals/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(festivalService.getFestivalById(anyInt())).thenThrow(new DatabaseException("database exception"));
            // Act & Assert
            mockMvc.perform(get("/festivals/1"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void testVenueIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(get("/festivals/notanint"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Arrange
            when(festivalService.getFestivalById(anyInt())).thenReturn(testFestival);
            // Act & Assert
            mockMvc.perform(get("/festivals/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetRespondsExpectedFestivalObject() throws Exception {
            // Arrange
            when(festivalService.getFestivalById(anyInt())).thenReturn(testFestival);
            // Act & Assert
            mockMvc.perform(get("/festivals/1"))
                    .andExpect(jsonPath("$.festival.name").value("Test Festival"));
        }

    }

    @Nested
    @DisplayName("deleteFestivalById controller tests")
    class DeleteFestivalByIdControllerTests {

        @Test
        void testFestivalIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(delete("/festivals/notanint"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testServiceReturnsFalseResponds404() throws Exception {
            // Arrange
            when(festivalService.deleteFestivalById(anyInt())).thenReturn(false);
            // Act & Assert
            mockMvc.perform(delete("/festivals/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(festivalService.deleteFestivalById(anyInt())).thenThrow(new DatabaseException("database exception"));
            // Act & Assert
            mockMvc.perform(delete("/festivals/1"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void testServiceReturnsTrueResponds204() throws Exception {
            // Arrange
            when(festivalService.deleteFestivalById(anyInt())).thenReturn(true);
            // Act & Assert
            mockMvc.perform(delete("/festivals/1"))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    @DisplayName("updateFestival controller tests")
    class UpdateFestivalControllerTests {

        @Test
        void testFestivalIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/notanint")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{" +
                                    "\"name\": \"Updated Test Festival\"," +
                                    " \"venueId\": 2, " +
                                    "\"year\": 2026, " +
                                    "\"month\": 2, " +
                                    "\"description\": \"Updated Test Description\"" +
                                    "}"
                    )).
                    andExpect(status().isBadRequest());

        }

        @Test
        void testNameEmptyResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"\"," +
                                            " \"venueId\": 2, " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            )).
                    andExpect(status().isBadRequest());
        }

        @Test
        void testNameMissingResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            " \"venueId\": 2, " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            )).
                    andExpect(status().isBadRequest());
        }

        @Test
        void testVenueIdNotIntResponds400() throws Exception {
            // Arrange
            // Act & Assert
            mockMvc.perform(patch("/festivals/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{" +
                                            "\"name\": \"Updated Test Festival\"," +
                                            " \"venueId\": \"not an int\", " +
                                            "\"year\": 2026, " +
                                            "\"month\": 2, " +
                                            "\"description\": \"Updated Test Description\"" +
                                            "}"
                            )).
                    andExpect(status().isBadRequest());
        }

    }
}
