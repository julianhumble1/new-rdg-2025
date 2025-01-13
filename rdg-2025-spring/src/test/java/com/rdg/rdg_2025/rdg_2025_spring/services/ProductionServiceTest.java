package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductionServiceTest {

    @InjectMocks
    private ProductionService productionService;

    @Mock
    private ProductionRepository productionRepository;

    @Mock
    private VenueRepository venueRepository;

    private NewProductionRequest testNewProductionRequest = new NewProductionRequest(
            "Test Production",
            1,
            "Test Author",
            "Test Description",
            LocalDateTime.now(),
            false,
            false,
            "Test File String"
    );

    private Production testProduction = new Production(
            "Test Production",
            new Venue(),
            "Test Author",
            "Test Description",
            LocalDateTime.now(),
            false,
            false,
            "Test File String"
    );

    @Nested
    @DisplayName("addNewProduction service tests")
    class addNewProductionServiceTests {

        @Test
        void testInvalidVenueIdThrowsEntityNotFoundException() {
            // Arrange
            when(venueRepository.findById(any())).thenReturn(Optional.empty());
            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                productionService.addNewProduction(testNewProductionRequest);
            });

        }

        @Test
        void testNewProductionWithFullDetailsIncludingVenueReturnsProductionObject() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(1)).thenReturn(Optional.of(testVenue));

            when(productionRepository.countByFieldNameStartingWith(any(String.class))).thenReturn(0);
            when(productionRepository.save(any(Production.class))).thenReturn(testProduction);
            // Act
            Production result = productionService.addNewProduction(testNewProductionRequest);
            // Assert
            assertEquals(testProduction, result);
        }

        @Test
        void testNewProductionWithOnlyNameReturnsProductionObject() {
            // Arrange
            NewProductionRequest onlyNameRequest = new NewProductionRequest(
                    "Test Production",
                    0, null, null, null, false, false, null);

            Production onlyNameProduction = new Production(
                    "Test Production",
                    null, null, null, null, false, false, null
            );

            when(productionRepository.countByFieldNameStartingWith(any(String.class))).thenReturn(0);
            when(productionRepository.save(any(Production.class))).thenReturn(onlyNameProduction);

            // Act
            Production result = productionService.addNewProduction(onlyNameRequest);

            // Assert
            assertEquals(onlyNameProduction, result);

        }

        @Nested
        @DisplayName("updateNameAndSlug tests")
        class updateNameAndSlugTests {

            static Method updateNameAndSlugIfRepeatPerformance;

            @BeforeAll
            static void getPrivateMethod() throws Exception {
                updateNameAndSlugIfRepeatPerformance = ProductionService.class.getDeclaredMethod("updateNameAndSlugIfRepeatPerformance", Production.class);
                updateNameAndSlugIfRepeatPerformance.setAccessible(true);
            }

            @Test
            void returnsOriginalProductionNameIfNeverPerformedBefore() throws Exception  {
                // Arrange
                when(productionRepository.countByFieldNameStartingWith(any())).thenReturn(0);

                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getName();

                // Assert
                assertEquals(testProduction.getName(), result);
            }

            @Test
            void returnsOriginalSlugIfNeverPerformedBefore() throws Exception {
                // Arrange
                when(productionRepository.countByFieldNameStartingWith(any())).thenReturn(0);

                String expected = "test-production";
                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getSlug();

                // Assert
                assertEquals(expected, result);
            }

            @Test
            void returnsNameWithTwoInBracketsIfPerformedOnceBefore() throws Exception {
                // Arrange
                when(productionRepository.countByFieldNameStartingWith(any())).thenReturn(1);

                String expected = testProduction.getName() + " (2)";
                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getName();

                // Assert
                assertEquals(expected, result);
            }

            @Test
            void returnSlugWithTwoOnEndIfPerformedOnceBefore() throws Exception {
                // Arrange
                when(productionRepository.countByFieldNameStartingWith(any())).thenReturn(1);

                String expected = "test-production-2";
                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getSlug();

                // Assert
                assertEquals(expected, result);
            }

        }

    }

}
