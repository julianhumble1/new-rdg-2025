package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.NewVenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.VenueService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @WithMockUser(roles="ADMIN")
    void testOkStatusWhenServiceSuccessfullySavesVenue() throws Exception{
        // Arrange
        Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
        when(venueService.addNewVenue(any(NewVenueRequest.class))).thenReturn(testVenue);

        // Act & Assert
        mockMvc.perform(post("/venues/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"name\": \"Test Venue\", \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                ))
                .andExpect(status().isOk()
                );
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void testReturnedVenueWhenServiceSuccessfullySavesVenue() throws Exception{
        // Arrange
        Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
        when(venueService.addNewVenue(any(NewVenueRequest.class))).thenReturn(testVenue);

        // Act & Assert
        mockMvc.perform(post("/venues/new")
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
        when(venueService.addNewVenue(any(NewVenueRequest.class))).thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        // Act & Assert
        mockMvc.perform(post("/venues/new")
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
        when(venueService.addNewVenue(any(NewVenueRequest.class))).thenThrow(new DatabaseException("Database Error"));

        // Act & Assert
        mockMvc.perform(post("/venues/new")
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
        mockMvc.perform(post("/venues/new")
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
        mockMvc.perform(post("/venues/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{ \"notes\": \"Test Notes\", \"postcode\": \"Test Postcode\", \"address\": \"Test Address\", " +
                                        "\"town\": \"Test Town\", \"url\": \"www.test.com\" }"
                        ))
                .andExpect(status().isBadRequest()
                );
    }

}
