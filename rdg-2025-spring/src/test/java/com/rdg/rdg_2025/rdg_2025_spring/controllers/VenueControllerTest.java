package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;

import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.VenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.VenueService;
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
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VenueController.class)
public class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VenueService venueService;

    @InjectMocks
    private VenueController venueController;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Nested
    @DisplayName("addNewVenue Controller Tests")
    class addNewVenueControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void test201StatusWhenServiceSuccessfullySavesVenue() throws Exception{
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueService.addNewVenue(any(VenueRequest.class))).thenReturn(testVenue);

            // Act & Assert
            mockMvc.perform(post("/venues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                    "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                    ))
                    .andExpect(status().isCreated()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testExpectedUriWhenSuccessfullySavesVenue() throws Exception{
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            int testVenueId = testVenue.getId();
            when(venueService.addNewVenue(any(VenueRequest.class))).thenReturn(testVenue);

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(header().string("Location", "/venues/" + testVenueId)
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testReturnedVenueWhenServiceSuccessfullySavesVenue() throws Exception{
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueService.addNewVenue(any(VenueRequest.class))).thenReturn(testVenue);

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                            .andExpect(jsonPath("$.venue.id").isNotEmpty())
                            .andExpect(jsonPath("$.venue.name").value(testVenue.getName()))
                            .andExpect(jsonPath("$.venue.notes").value(testVenue.getNotes()))
                            .andExpect(jsonPath("$.venue.postcode").value(testVenue.getPostcode()))
                            .andExpect(jsonPath("$.venue.address").value(testVenue.getAddress()))
                            .andExpect(jsonPath("$.venue.town").value(testVenue.getTown()))
                            .andExpect(jsonPath("$.venue.url").value(testVenue.getUrl()))
                            .andExpect(jsonPath("$.venue.createdAt").isNotEmpty())
                            .andExpect(jsonPath("$.venue.updatedAt").isNotEmpty())
                            .andExpect(jsonPath("$.venue.slug").isNotEmpty()

                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDataIntegrityViolationExceptionReturns409Error() throws Exception{
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueService.addNewVenue(any(VenueRequest.class))).thenThrow(new DataIntegrityViolationException("Data integrity violation"));

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isConflict()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDataBaseExceptionReturns500Error() throws Exception{
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueService.addNewVenue(any(VenueRequest.class))).thenThrow(new DatabaseException("Database Error"));

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isInternalServerError()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testEmptyNameReturns400BadRequest() throws Exception{

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testMissingNameReturns400BadRequest() throws Exception{

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                            "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testNameWithMissingOtherValuesReturns201() throws Exception{
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.addNewVenue(any(VenueRequest.class))).thenReturn(testVenue);

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Venue\" }"
                            ))
                    .andExpect(status().isCreated()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testNameWithEmptyOtherValuesReturns201() throws Exception{
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.addNewVenue(any(VenueRequest.class))).thenReturn(testVenue);

            // Act & Assert
            mockMvc.perform(post("/venues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Venue\", \"notes\": \"\", \"postcode\": \"\", \"address\": \"\", " +
                                            "\"town\": \"\", \"url\": \"\" }"
                            ))
                    .andExpect(status().isCreated()
                    );
        }

    }

    @Nested
    @DisplayName("getAllVenues Controller Tests")
    class getAllVenuesControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void testSuccessfulGetReturns200Code() throws Exception {
            // Arrange
            ArrayList<Venue> venues = new ArrayList<>();
            when(venueService.getAllVenues()).thenReturn(venues);
            // Act & Assert
            mockMvc.perform(get("/venues"))
                            .andExpect(status().isOk());

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testSuccessfulGetResponseContainsVenue() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            ArrayList<Venue> venues = new ArrayList<>();
            venues.add(testVenue);

            when(venueService.getAllVenues()).thenReturn(venues);
            // Act & Assert
            mockMvc.perform(get("/venues"))
                    .andExpect(jsonPath("$.venues[0].name").value("Test Venue"));

        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDataBaseExceptionReturns500Error() throws Exception{
            // Arrange
            when(venueService.getAllVenues()).thenThrow(new DatabaseException("Database Error"));

            // Act & Assert
            mockMvc.perform(get("/venues"))
                    .andExpect(status().isInternalServerError());
        }

    }

    @Nested
    @DisplayName("deleteVenueById Controller Tests")
    class deleteVenueByControllerTests {

        @Test
        @WithMockUser(roles = "ADMIN")
        void testSuccessfulDeletionResponds204() throws Exception {
            // Arrange
            when(venueService.deleteVenueById(anyInt())).thenReturn(true);
            // Act & Assert
            mockMvc.perform(delete("/venues/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testNonExistentVenueResponds404() throws Exception {
            // Arrange
            when(venueService.deleteVenueById(anyInt())).thenReturn(false);
            // Act & Assert
            mockMvc.perform(delete("/venues/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(venueService.deleteVenueById(anyInt())).thenThrow(new DatabaseException("Database Exception"));
            // Act & Assert
            mockMvc.perform(delete("/venues/1"))
                    .andExpect(status().isInternalServerError());
        }

    }

    @Nested
    @DisplayName("getVenueById Controller Tests")
    class getVenueByIdControllerTests {

        @Test
        void testSuccessfulGetReturns200Code() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.getVenueById(anyInt())).thenReturn(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void testSuccessfulGetReturnsVenueObject() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.getVenueById(anyInt())).thenReturn(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(jsonPath("$.venue.name").value("Test Venue"));
        }

        @Test
        void testDatabaseExceptionResponds500() throws Exception {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenThrow(new DatabaseException("database exception"));
            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        void testEntityNotFoundExceptionResponds404() throws Exception {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenThrow(new EntityNotFoundException("Venue id does not exist"));
            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testSuccessfulGetVenueReturnsProductionsArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.getVenueById(anyInt())).thenReturn(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(jsonPath("$.productions").isArray());
        }

        @Test
        void testSuccessfulGetVenueReturnsExpectedProductionsInArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.getVenueById(anyInt())).thenReturn(testVenue);
            Production testProduction = new Production(
                    "Test Production", testVenue, null, null, null, false, false, null
            );
            testVenue.setProductions(Arrays.asList(testProduction));
            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(jsonPath("$.productions[0].name").value("Test Production"));
        }

        @Test
        void testSuccessfulGetVenueReturnsFestivalsArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.getVenueById(anyInt())).thenReturn(testVenue);
            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(jsonPath("$.festivals").isArray());
        }

        @Test
        void testSuccessfulGetVenueReturnsExpectedFestivalsInArray() throws Exception {
            // Arrange
            Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
            when(venueService.getVenueById(anyInt())).thenReturn(testVenue);
            Festival testFestival = new Festival("Test Festival", testVenue, 2025, 1, null);
            testVenue.setFestivals(Arrays.asList(testFestival));

            // Act & Assert
            mockMvc.perform(get("/venues/1"))
                    .andExpect(jsonPath("$.festivals[0].name").value("Test Festival"));
        }

    }

    @Nested
    @DisplayName("updateVenue Controller Tests")
    class updateVenueControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void successfulUpdateResponds200() throws Exception {
            // Arrange
            Venue updatedTestVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueService.updateVenue(anyInt(), any(VenueRequest.class))).thenReturn(updatedTestVenue);
            // Act & Assert
            mockMvc.perform(patch("/venues/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                    "{ \"name\": \"Updated Test Venue\", \"notes\": \"Updated Test Notes\", \"postcode\": \"Updated Test Postcode\", \"address\": \"Updated Test Address\", " +
                            "\"town\": \"Updated Test Town\", \"url\": \"www.updatedtest.com\" }"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void successfulUpdateRespondsExpectedVenueObject() throws Exception {
            // Arrange
            Venue updatedTestVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueService.updateVenue(anyInt(), any(VenueRequest.class))).thenReturn(updatedTestVenue);
            // Act & Assert
            mockMvc.perform(patch("/venues/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Updated Test Venue\", \"notes\": \"Updated Test Notes\", \"postcode\": \"Updated Test Postcode\", \"address\": \"Updated Test Address\", " +
                                            "\"town\": \"Updated Test Town\", \"url\": \"www.updatedtest.com\" }"))
                    .andExpect(jsonPath("$.venue.name").value("Test Venue"));
        }


    }


}
