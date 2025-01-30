package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.performance.PerformanceRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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
                new BigDecimal(10.00),
                new BigDecimal(9.00),
                "Test Box Office"
        );
        testPerformance = new Performance(
                new Production(),
                new Venue(),
                new Festival(),
                LocalDateTime.MAX,
                "Test Performance Description",
                new BigDecimal(10.00),
                new BigDecimal(9.00),
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


    }



}
