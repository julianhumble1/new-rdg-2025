package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festival.FestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FestivalServiceTest {

    VenueRepository venueRepository;

    @Mock
    FestivalRepository festivalRepository;

    @Mock
    VenueService venueService;

    @Mock
    PerformanceService performanceService;

    @InjectMocks
    FestivalService festivalService;

    private FestivalRequest testFestivalRequest = new FestivalRequest(
            "Test Festival", 1, 2025, 4, "Test Description"
    );

    private Festival testFestival = new Festival(
            "Test Festival", new Venue(), 2025, 4, "Test Description"
    );

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("addNewFestival service tests")
    class AddNewFestivalServiceTests{

        @Test
        void testInvalidVenueIdThrowsEntityNotFoundException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenThrow(new EntityNotFoundException());

            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                festivalService.addNewFestival(testFestivalRequest);
            });
        }

        @Test
        void testNewFestivalWithFullDetailsIncludingVenueReturnsFestivalObject() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueService.getVenueById(anyInt())).thenReturn(testVenue);

            when(festivalRepository.save(any(Festival.class))).thenReturn(testFestival);

            // Act
            Festival result = festivalService.addNewFestival(testFestivalRequest);
            // Assert
            assertEquals(testFestival, result);

        }

        @Test
        void testNewFestivalWithOnlyNameAndYearReturnsFestivalObject() {
            // Arrange
            FestivalRequest onlyNameAndYearFestivalRequest = new FestivalRequest(
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
            when(venueService.getVenueById(1)).thenThrow(new DatabaseException("Database Error") {});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () ->
                    festivalService.addNewFestival(testFestivalRequest)
            );
        }

        @Test
        void testFestivalSaveDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueService.getVenueById(1)).thenReturn(testVenue);

            when(festivalRepository.save(any(Festival.class))).thenThrow(
                    new DataAccessException("Data Access Error") {}
            );

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () ->
                festivalService.addNewFestival(testFestivalRequest)
            );

        }

        @Test
        void testFestivalSavePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueService.getVenueById(1)).thenReturn(testVenue);

            when(festivalRepository.save(any(Festival.class))).thenThrow(
                    new PersistenceException("Persistence Error") {}
            );

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () ->
                    festivalService.addNewFestival(testFestivalRequest)
            );

        }

    }

    @Nested
    @DisplayName("getAllFestivals service tests")
    class GetAllFestivalsServiceTests {

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

        @Test
        void testGetAllProductionsWithMultipleFestivalsInDatabaseReturnsExpectedListLength() {
            // Arrange
            ArrayList<Festival> testFestivalList = new ArrayList<>();
            testFestivalList.add(testFestival);
            Festival testFestival2 = new Festival();
            testFestivalList.add(testFestival2);
            when(festivalRepository.findAll()).thenReturn(testFestivalList);
            // Act
            List<Festival> festivals = festivalService.getAllFestivals();
            // Assert
            assertEquals(2, festivals.size());

        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(festivalRepository.findAll()).thenThrow(new DataAccessException("Data access failed"){});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                festivalService.getAllFestivals();
            });
        }

    }

    @Nested
    @DisplayName("getFestivalById service tests")
    class GetFestivalByIdServiceTests {

        @Test
        void testReturnsFoundFestivalWhenFestivalExists() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            // Act
            Festival festival = festivalService.getFestivalById(testFestival.getId());
            // Assert
            assertEquals(testFestival, festival);
        }

        @Test
        void testFestivalDoesNotExistThrowsEntityNotFoundException() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.empty());
            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                festivalService.getFestivalById(testFestival.getId());
            });
        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenThrow(new DataAccessException("data access exception") {});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                festivalService.getFestivalById(testFestival.getId());
            });
        }

    }

    @Nested
    @DisplayName("setFestivalVenueFieldToNull service tests")
    class SetFestivalVenueFieldToNullServiceTests {

        @Test
        void testSaveFestivalIsCalled() {
            // Arrange
            // Act
            festivalService.setFestivalVenueFieldToNull(testFestival);
            // Assert
            verify(festivalRepository, atLeastOnce()).save(testFestival);
        }
    }

    @Nested
    @DisplayName("deleteFestivalById service tests")
    class DeleteFestivalByIdServiceTests {

        @Test
        void testIfFestivalIsNotInDatabaseThenReturnsFalse() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenThrow(new EntityNotFoundException("Festival does not exist"));
            // Act
            boolean result = festivalService.deleteFestivalById(1);
            // Assert
            assertEquals(false, result);
        }

        @Test
        void testGetFestivalDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenThrow(new DataAccessException("database exception"){});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                festivalService.deleteFestivalById(1);
            });
        }

        @Test
        void testFestivalExistsThenRemoveFromVenueFestivalListIsCalled() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            // Act
            festivalService.deleteFestivalById(1);
            // Assert
            verify(venueService, times(1)).removeFestivalFromVenueFestivalList(any());
        }

        @Test
        void testFestivalHasNoPerformancesThenNoPerformanceServiceCall() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            // Act
            festivalService.deleteFestivalById(1);
            // Assert
            verify(performanceService, never()).setPerformanceFestivalFieldToNull(any());
        }

        @Test
        void testFestivalHasOnePerformancesThenPerformanceServiceCalledOnce() {
            // Arrange
            List<Performance> performanceList = new ArrayList<>();
            Performance testPerformance = new Performance();
            performanceList.add(testPerformance);
            testFestival.setPerformances(performanceList);
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            // Act
            festivalService.deleteFestivalById(1);
            // Assert
            verify(performanceService, times(1)).setPerformanceFestivalFieldToNull(any());
        }

        @Test
        void testFestivalHasMultiplePerformancesThenPerformanceServiceCalledMultipleTimes() {
            // Arrange
            List<Performance> performanceList = new ArrayList<>();
            Performance testPerformance1 = new Performance();
            Performance testPerformance2 = new Performance();
            performanceList.add(testPerformance1);
            performanceList.add(testPerformance2);
            testFestival.setPerformances(performanceList);
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            // Act
            festivalService.deleteFestivalById(1);
            // Assert
            verify(performanceService, times(2)).setPerformanceFestivalFieldToNull(any());
        }

        @Test
        void testFestivalServiceThrowsDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            List<Performance> performanceList = new ArrayList<>();
            Performance testPerformance = new Performance();
            performanceList.add(testPerformance);
            testFestival.setPerformances(performanceList);
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            doThrow(new DatabaseException("database exception")).when(performanceService).setPerformanceFestivalFieldToNull(any());
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
               festivalService.deleteFestivalById(1);
            });
        }

        @Test
        void testFestivalExistsThenDeleteFestivalIsCalled() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            // Act
            festivalService.deleteFestivalById(1);
            // Assert
            verify(festivalRepository, times(1)).delete(any());
        }

        @Test
        void testDeleteThrowsDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            doThrow(new DataAccessException("data access exception") {}).when(festivalRepository).delete(any());

            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                festivalService.deleteFestivalById(1);
            });
        }

        @Test
        void testDeleteThrowsPersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            doThrow(new PersistenceException("persistence exception")).when(festivalRepository).delete(any());

            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                festivalService.deleteFestivalById(1);
            });
        }

        @Test
        void testSuccessfulDeletionReturnsTrue() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenReturn(Optional.of(testFestival));
            // Act
            boolean result = festivalService.deleteFestivalById(1);
            // Assert
            assertTrue(result);
        }

    }

    @Nested
    @DisplayName("updateFestival service tests")
    class UpdateFestivalServiceTests {

        private FestivalRequest testUpdateFestivalRequest;

        @BeforeEach
        void beforeEach() {
            testUpdateFestivalRequest = new FestivalRequest(
                    "Updated Festival Name",
                    2,
                    2026,
                    2,
                    "Updated Festival Description"
            );
        }

        @Test
        void testGetFestivalDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(festivalRepository.findById(anyInt())).thenThrow(new DataAccessException("data access exception") {});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                festivalService.updateFestival(1, testUpdateFestivalRequest);
            });
        }


    }
}
