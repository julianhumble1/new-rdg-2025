package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.NewVenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void testDuplicateVenueNameThrowsDataIntegrityException() {
        // Arrange
        when(venueRepository.save(any(Venue.class))).thenThrow(new DataIntegrityViolationException("Duplicate venue name"));

        // Act & Assert
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {
            venueService.addNewVenue(new NewVenueRequest(
                    "Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com")
            );
        });
    }

    @Test
    void testDataAccessExceptionThrowsDatabaseException() {
        // Arrange
        when(venueRepository.save(any(Venue.class))).thenThrow(new DataAccessException("Data access failed"){});

        // Act & Assert
        DatabaseException ex = assertThrows(DatabaseException.class, () -> {
            venueService.addNewVenue(new NewVenueRequest(
                    "Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com")
            );
        });
    }

    @Test
    void testPersistenceExceptionThrowsDatabaseException() {
        // Arrange
        when(venueRepository.save(any(Venue.class))).thenThrow(new PersistenceException("Persistence exception"));

        // Act & Assert
        DatabaseException ex = assertThrows(DatabaseException.class, () -> {
            venueService.addNewVenue(new NewVenueRequest(
                    "Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com")
            );
        });
    }

}
