package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.NewVenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    @InjectMocks
    private VenueService venueService;

    @Mock
    private VenueRepository venueRepository;

    @Test
    void testAddNewVenueWithAllFieldsSuccessReturnsExpectedVenueObject() {
        // Arrange
        Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
        when(venueRepository.save(any(Venue.class))).thenReturn(testVenue);

        // Act
        Venue result = venueService.addNewVenue(
                new NewVenueRequest("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com")
        );

        // Assert
        assertEquals(testVenue, result);
    }

    @Test
    void testAddNewVenueWithNoFieldsExceptNameSuccessReturnsExpectedVenueObject() {
        // Arrange
        Venue testVenue = new Venue("Test Venue", null, null, null, null, null);
        when(venueRepository.save(any(Venue.class))).thenReturn(testVenue);

        // Act
        Venue result = venueService.addNewVenue(
                new NewVenueRequest("Test Venue", null, null, null, null, null)
        );

        // Assert
        assertEquals(testVenue, result);
    }

}
