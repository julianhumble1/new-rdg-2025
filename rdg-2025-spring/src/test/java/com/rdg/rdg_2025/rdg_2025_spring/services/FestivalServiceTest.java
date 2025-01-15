package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals.NewFestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FestivalServiceTest {

    @InjectMocks
    FestivalService festivalService;

    @Mock
    FestivalRepository festivalRepository;

    @Mock
    VenueRepository venueRepository;

    private NewFestivalRequest testNewFestivalRequest = new NewFestivalRequest(
            "Test Festival", 1, 2025, 4, "Test Description"
    );

    private Festival testFestival = new Festival(
            "Test Festival", new Venue(), 2025, 4, "Test Description"
    );

    @Nested
    @DisplayName("addNewFestival service tests")
    class addNewFestivalServiceTests{

        @Test
        void testInvalidVenueIdThrowsEntityNotFoundException() {
            // Arrange
            when(venueRepository.findById(any())).thenThrow(EntityNotFoundException.class);

            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                festivalService.addNewFestival(testNewFestivalRequest);
            });
        }

        @Test
        void testNewFestivalWithFullDetailsIncludingVenueReturnsFestivalObject() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(any())).thenReturn(Optional.of(testVenue));

            when(festivalRepository.save(any(Festival.class))).thenReturn(testFestival);

            // Act
            Festival result = festivalService.addNewFestival(testNewFestivalRequest);
            // Assert
            assertEquals(testFestival, result);

        }

        @Test
        void testNewFestivalWithOnlyNameAndYearReturnsFestivalObject() {
            // Arrange
            NewFestivalRequest onlyNameAndYearFestivalRequest = new NewFestivalRequest(
                    "Test Festival", 0, 2025, 0, "Test Description"
            );

            Festival onlyNameAndYearFestival= new Festival(
                    "Test Festival", new Venue(), 2025, 0, "Test Description"
            );

            when(festivalRepository.save(any(Festival.class))).thenReturn(onlyNameAndYearFestival);

            // Act
            Festival result = festivalService.addNewFestival(onlyNameAndYearFestivalRequest);
            // Assert
            assertEquals(onlyNameAndYearFestival, result);

        }

        @Test
        void testVenueFindDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findById(1)).thenThrow(new DataAccessException("Data Access Error") {});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () ->
                    festivalService.addNewFestival(testNewFestivalRequest)
            );
        }

        @Test
        void testFestivalSaveDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(1)).thenReturn(Optional.of(testVenue));

            when(festivalRepository.save(any(Festival.class))).thenThrow(
                    new DataAccessException("Data Access Error") {}
            );

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () ->
                festivalService.addNewFestival(testNewFestivalRequest)
            );

        }

        @Test
        void testFestivalSavePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(1)).thenReturn(Optional.of(testVenue));

            when(festivalRepository.save(any(Festival.class))).thenThrow(
                    new PersistenceException("Persistence Error") {}
            );

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () ->
                    festivalService.addNewFestival(testNewFestivalRequest)
            );

        }

    }

    @Nested
    @DisplayName("getAllFestivals service tests")
    class getAllFestivalsServiceTests {

        @Test
        void testGetAllFestivalsWithEmptyDatabaseReturnsEmptyList() {
            // Arrange
            when(festivalRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
            // Act
            List<Festival> festivals = festivalService.getAllFestivals();
            // Assert
            assertEquals(Collections.EMPTY_LIST, festivals);

        }

        @Test
        void testGetAllFestivalsWithOneFestivalInDatabaseReturnsListLengthOne() {
            // Arrange
            ArrayList<Festival> testFestivalList = new ArrayList<>();
            testFestivalList.add(testFestival);
            when(festivalRepository.findAll()).thenReturn(testFestivalList);
            // Act
            List<Festival> festivals = festivalService.getAllFestivals();
            // Assert
            assertEquals(1, festivals.size());

        }

        @Test
        void testGetAllFestivalsWithOneFestivalInDatabaseReturnsExpectedFestival() {
            // Arrange
            ArrayList<Festival> testFestivalList = new ArrayList<>();
            testFestivalList.add(testFestival);
            when(festivalRepository.findAll()).thenReturn(testFestivalList);
            // Act
            List<Festival> festivals = festivalService.getAllFestivals();
            // Assert
            assertEquals(testFestival, festivals.get(0));

        }

    }

}
