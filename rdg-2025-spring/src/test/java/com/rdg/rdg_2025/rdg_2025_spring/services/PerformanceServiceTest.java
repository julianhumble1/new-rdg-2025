package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.performance.PerformanceRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PerformanceRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTest {

    @Mock
    PerformanceRepository performanceRepository;

    @Mock
    ProductionService productionService;

    @Mock
    VenueService venueService;

    @Mock
    FestivalService festivalService;

    @InjectMocks
    PerformanceService performanceService;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    private PerformanceRequest testPerformanceRequest;
    private Performance testPerformance;

    @BeforeEach
    void setup() {
        testPerformanceRequest = new PerformanceRequest(
                1,
                1,
                1,
                LocalDateTime.MAX,
                "Test Performance Description",
                new BigDecimal("10.00"),
                new BigDecimal("9.00"),
                "Test Box Office"
        );
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
    @DisplayName("addNewPerformance service tests")
    class AddNewPerformanceServiceTests {

        @Test
        void testNonExistentVenueIdProvidedThrowsEntityNotFoundException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenThrow(new EntityNotFoundException());
            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testVenueServiceDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenThrow(new DatabaseException("database exception"));
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testNonExistentProductionIdProvidedThrowsEntityNotFoundException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenThrow(new EntityNotFoundException());
            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testProductionServiceDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenThrow(new DatabaseException("database exception"));
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testIfNoFestivalProvidedThenNoCallToServiceIsMade() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenReturn(new Production());
            // Act
            PerformanceRequest noFestivalRequest = new PerformanceRequest(
                    1,
                    1,
                    0,
                    LocalDateTime.MAX,
                    "Test Performance Description",
                    new BigDecimal("10.00"),
                    new BigDecimal("9.00"),
                    "Test Box Office"
            );
            performanceService.addNewPerformance(noFestivalRequest);
            // Assert
            verify(festivalService, never()).getFestivalById(anyInt());
        }

        @Test
        void testNonExistentFestivalIdThrowsEntityNotFoundException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenReturn(new Production());
            when(festivalService.getFestivalById(anyInt())).thenThrow(new EntityNotFoundException());
            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
               performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testFestivalServiceDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenReturn(new Production());
            when(festivalService.getFestivalById(anyInt())).thenThrow(new DatabaseException("Database exception"));
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testSaveDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenReturn(new Production());
            when(festivalService.getFestivalById(anyInt())).thenReturn(new Festival());

            when(performanceRepository.save(any())).thenThrow(new DataAccessException("Data access exception") {});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testSavePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenReturn(new Production());
            when(festivalService.getFestivalById(anyInt())).thenReturn(new Festival());

            when(performanceRepository.save(any())).thenThrow(new PersistenceException());
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                performanceService.addNewPerformance(testPerformanceRequest);
            });
        }

        @Test
        void testSuccessfulSaveReturnsExpectedPerformance() {
            // Arrange
            when(venueService.getVenueById(anyInt())).thenReturn(new Venue());
            when(productionService.getProductionById(anyInt())).thenReturn(new Production());
            when(festivalService.getFestivalById(anyInt())).thenReturn(new Festival());

            when(performanceRepository.save(any())).thenReturn(testPerformance);
            // Act
            Performance actual = performanceService.addNewPerformance(testPerformanceRequest);
            // Assert
            assertEquals(testPerformance, actual);
        }


    }

    @Nested
    @DisplayName("setPerformanceFestivalFieldToNull service tests")
    class SetPerformanceFestivalFieldToNullServiceTests {

        @Test
        void testSavePerformanceIsCalled() {
            // Arrange

            // Act
            performanceService.setPerformanceFestivalFieldToNull(testPerformance);
            // Assert
            verify(performanceRepository, times(1)).save(any());
        }


    }


    @Nested
    @DisplayName("deletePerformanceById service tests")
    class DeletePerformanceByIdServiceTests {

        @Test
        void testIfPerformanceNotInDatabaseThenThrowsEntityNotFoundException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenThrow(new EntityNotFoundException("No performance with this id"));
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                performanceService.deletePerformanceById(1);
            });

        }

        @Test
        void testPerformanceExistsDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenThrow(new DataAccessException("Data access exception") {});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                performanceService.deletePerformanceById(1);
            });

        }

        @Test
        void testRemovePerformanceFromProductionsPerformancesIsCalled() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            // Act
            performanceService.deletePerformanceById(1);
            // Assert
            verify(productionService, times(1)).removePerformanceFromProductionPerformanceList(testPerformance);
        }

        @Test
        void testRemovePerformanceFromProductionDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            doThrow(new DatabaseException("database exception")).when(productionService).removePerformanceFromProductionPerformanceList(any());
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                performanceService.deletePerformanceById(1);
            });
        }

        @Test
        void testRemovePerformanceVenueServiceMethodIsCalled() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            // Act
            performanceService.deletePerformanceById(1);
            // Assert
            verify(venueService, times(1)).removePerformanceFromVenuePerformanceList(testPerformance);
        }

        @Test
        void testRemovePerformanceFromVenueDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            doThrow(new DatabaseException("database exception")).when(venueService).removePerformanceFromVenuePerformanceList(testPerformance);
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                performanceService.deletePerformanceById(1);
            });
        }

        @Test
        void testRemovePerformanceFestivalServiceMethodIsCalled() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            // Act
            performanceService.deletePerformanceById(1);
            // Assert
            verify(festivalService, times(1)).removePerformanceFromFestivalPerformanceList(testPerformance);
        }

        @Test
        void testRemovePerformanceFromFestivalDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            doThrow(new DatabaseException("database exception")).when(festivalService).removePerformanceFromFestivalPerformanceList(testPerformance);
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                performanceService.deletePerformanceById(1);
            });
        }

        @Test
        void testPerformanceRepositoryDeleteMethodIsCalled() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            // Act
            performanceService.deletePerformanceById(1);
            // Assert
            verify(performanceRepository, times(1)).delete(testPerformance);
        }

        @Test
        void testDeletePerformanceDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            doThrow(new DataAccessException("Data access exception") {}).when(performanceRepository).delete(testPerformance);
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                performanceService.deletePerformanceById(1);
            });
        }

        @Test
        void testDeletePerformancePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            doThrow(new PersistenceException()).when(performanceRepository).delete(testPerformance);
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                performanceService.deletePerformanceById(1);
            });
        }

        @Test
        void testSuccessfulDeleteDoesNotThrowException() {
            // Arrange
            when(performanceRepository.findById(anyInt())).thenReturn(Optional.of(testPerformance));
            // Act & Assert
            assertDoesNotThrow(() -> {
                performanceService.deletePerformanceById(1);
            });
        }


    }

}
