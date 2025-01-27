package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.ProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductionServiceTest {

    @InjectMocks
    private ProductionService productionService;

    @Mock
    private ProductionRepository productionRepository;

    @Mock
    private VenueRepository venueRepository;

    private ProductionRequest testProductionRequest = new ProductionRequest(
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
                productionService.addNewProduction(testProductionRequest);
            });

        }

        @Test
        void testNewProductionWithFullDetailsIncludingVenueReturnsProductionObject() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(1)).thenReturn(Optional.of(testVenue));

            when(productionRepository.countByNameStartingWith(any(String.class))).thenReturn(0);
            when(productionRepository.save(any(Production.class))).thenReturn(testProduction);
            // Act
            Production result = productionService.addNewProduction(testProductionRequest);
            // Assert
            assertEquals(testProduction, result);
        }

        @Test
        void testNewProductionWithOnlyNameReturnsProductionObject() {
            // Arrange
            ProductionRequest onlyNameRequest = new ProductionRequest(
                    "Test Production",
                    0, null, null, null, false, false, null);

            Production onlyNameProduction = new Production(
                    "Test Production",
                    null, null, null, null, false, false, null
            );

            when(productionRepository.countByNameStartingWith(any(String.class))).thenReturn(0);
            when(productionRepository.save(any(Production.class))).thenReturn(onlyNameProduction);

            // Act
            Production result = productionService.addNewProduction(onlyNameRequest);

            // Assert
            assertEquals(onlyNameProduction, result);

        }

        @Test
        void testVenueDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findById(1)).thenThrow(new DataAccessException("Data access failed"){});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> productionService.addNewProduction(testProductionRequest));
        }

        @Test
        void testProductionDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(1)).thenReturn(Optional.of(testVenue));

            when(productionRepository.countByNameStartingWith(any(String.class))).thenReturn(0);
            when(productionRepository.save(any(Production.class))).thenThrow(new DataAccessException("Data access failed"){});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> productionService.addNewProduction(testProductionRequest));
        }

        @Test
        void testProductionPersistenceExceptionThrowsDatabaseException () {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(1)).thenReturn(Optional.of(testVenue));

            when(productionRepository.countByNameStartingWith(any(String.class))).thenReturn(0);
            when(productionRepository.save(any(Production.class))).thenThrow(new PersistenceException("Data persistence failed"){});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> productionService.addNewProduction(testProductionRequest));
        }

        @Test
        void testDuplicateProductionNameThrowsDataIntegrityViolationException() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(1)).thenReturn(Optional.of(testVenue));

            when(productionRepository.countByNameStartingWith(any(String.class))).thenReturn(0);
            when(productionRepository.save(any(Production.class))).thenThrow(new DataIntegrityViolationException("Duplicate name"){});

            // Act & Assert
            DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> productionService.addNewProduction(testProductionRequest));
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
                when(productionRepository.countByNameStartingWith(any())).thenReturn(0);

                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getName();

                // Assert
                assertEquals(testProduction.getName(), result);
            }

            @Test
            void returnsOriginalSlugIfNeverPerformedBefore() throws Exception {
                // Arrange
                when(productionRepository.countByNameStartingWith(any())).thenReturn(0);

                String expected = "test-production";
                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getSlug();

                // Assert
                assertEquals(expected, result);
            }

            @Test
            void returnsNameWithTwoInBracketsIfPerformedOnceBefore() throws Exception {
                // Arrange
                when(productionRepository.countByNameStartingWith(any())).thenReturn(1);

                String expected = testProduction.getName() + " (2)";
                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getName();

                // Assert
                assertEquals(expected, result);
            }

            @Test
            void returnSlugWithTwoOnEndIfPerformedOnceBefore() throws Exception {
                // Arrange
                when(productionRepository.countByNameStartingWith(any())).thenReturn(1);

                String expected = "test-production-2";
                // Act
                String result = ((Production) updateNameAndSlugIfRepeatPerformance.invoke(productionService, testProduction)).getSlug();

                // Assert
                assertEquals(expected, result);
            }

        }

    }

    @Nested
    @DisplayName("getAllProductions service tests")
    class getAllProductionsServiceTests{

        @Test
        void testGetAllProductionsWithEmptyDatabaseReturnsEmptyList() {
            // Arrange
            when(productionRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

            // Act
            List<Production> productions = productionService.getAllProductions();
            // Assert
            assertEquals(Collections.EMPTY_LIST, productions);

        }

        @Test
        void testGetAllProductionsWithOneProductionInDatabaseReturnsListLengthOne() {
            // Arrange
            Production testProduction = new Production(
                    "Test Production",
                    new Venue(), null, null, null,  false, false, null
            );
            ArrayList<Production> testProductionList = new ArrayList<>();
            testProductionList.add(testProduction);
            when(productionRepository.findAll()).thenReturn(testProductionList);

            // Act
            List<Production> productions = productionService.getAllProductions();
            // Assert
            assertEquals(1, productions.size());

        }

        @Test
        void testGetAllProductionsWithOneProductionInDatabaseReturnsExpectedProduction() {
            // Arrange
            Production testProduction = new Production(
                    "Test Production",
                    new Venue(), null, null, null,  false, false, null
            );
            ArrayList<Production> testProductionList = new ArrayList<>();
            testProductionList.add(testProduction);
            when(productionRepository.findAll()).thenReturn(testProductionList);

            // Act
            List<Production> productions = productionService.getAllProductions();
            // Assert
            assertEquals(testProduction, productions.get(0));

        }

        @Test
        void testGetAllProductionsMultipleProductionsInDatabaseReturnsExpectedLength() {
            // Arrange
            Production testProduction1 = new Production(
                    "Test Production",
                    new Venue(), null, null, null,  false, false, null
            );
            Production testProduction2 = new Production(
                    "Another Test Production",
                    new Venue(), null, null, null,  false, false, null
            );
            ArrayList<Production> testProductionList = new ArrayList<>();
            testProductionList.add(testProduction1);
            testProductionList.add(testProduction2);
            when(productionRepository.findAll()).thenReturn(testProductionList);

            // Act
            List<Production> productions = productionService.getAllProductions();
            // Assert
            assertEquals(2, productions.size());

        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(productionRepository.findAll()).thenThrow(new DataAccessException("Data access failed"){});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                productionService.getAllProductions();
            });
        }

        @Test
        void testPersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(productionRepository.findAll()).thenThrow(new PersistenceException("Data persistence failed"){});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                productionService.getAllProductions();
            });
        }


    }

    @Nested
    @DisplayName("getProductionById service tests")
    class getProductionByIdServiceTests {

        @Test
        void testSuccessfulGetReturnsProductionObject() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.of(testProduction));
            // Act
            Production retrivedProduction = productionService.getProductionById(1);
            // Assert
            assertEquals(testProduction, retrivedProduction);
        }

        @Test
        void testNonExistentProductionIdThrowsEntityNotFoundException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                productionService.getProductionById(1);
            });
        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenThrow(new DataAccessException("data access exception") {
            });

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                productionService.getProductionById(1);
            });
        }

    }

    @Nested
    @DisplayName("updateProduction service tests")
    class updateProductionServiceTests {

        ProductionRequest testUpdateProductionRequest = new ProductionRequest(
                "Updated Test Production",
                1,
                "Updated Test Author",
                "Updated Test Description",
                LocalDateTime.now(),
                true,
                true,
                "Updated File String"
        );

        @Test
        void testProductionIdNotValidThrowsEntityNotFoundException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.empty());
            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
               productionService.updateProduction(1, testUpdateProductionRequest);
            });

        }

        @Test
        void testProductionIdCheckDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenThrow(new DataAccessException("Data access exception") {});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                productionService.updateProduction(1, testUpdateProductionRequest);
            });

        }

        @Test
        void testIfVenueIdProvidedButDoesNotExistThrowsEntityNotFoundException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.of(testProduction));
            when(venueRepository.findById(anyInt())).thenReturn(Optional.empty());
            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                productionService.updateProduction(1, testUpdateProductionRequest);
            });
        }

        @Test
        void testVenueExistsCheckDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.of(testProduction));
            when(venueRepository.findById(anyInt())).thenThrow(new DataAccessException("Data Access Exception") {});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                productionService.updateProduction(1, testUpdateProductionRequest);
            });
        }

        @Test
        void testNoVenueIdProvidedThenNoCallToVenueRepository() {
            // Arrange
            ProductionRequest noVenueProductionRequest = new ProductionRequest(
                    "Updated Test Production",
                    0,
                    "Updated Test Author",
                    "Updated Test Description",
                    LocalDateTime.now(),
                    true,
                    true,
                    "Updated File String"
            );

            when(productionRepository.findById(anyInt())).thenReturn(Optional.of(testProduction));
            // Act
            productionService.updateProduction(1, noVenueProductionRequest);
            // Assert
            verify(venueRepository, never()).findById(anyInt());
        }

        @Test
        void testSaveNewProductionDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.of(testProduction));
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(new Venue()));

            when(productionRepository.save(any(Production.class))).thenThrow(new DataAccessException("Data Access Exception") {});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                productionService.updateProduction(1, testUpdateProductionRequest);
            });
        }

        @Test
        void testSaveNewProductionPersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.of(testProduction));
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(new Venue()));

            when(productionRepository.save(any(Production.class))).thenThrow(new PersistenceException("Persistence exception"));
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                productionService.updateProduction(1, testUpdateProductionRequest);
            });
        }

        @Test
        void testSaveNewProductionIntegrityViolationThrowsDataIntegrityViolationException() {
            // Arrange
            when(productionRepository.findById(anyInt())).thenReturn(Optional.of(testProduction));
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(new Venue()));

            when(productionRepository.save(any(Production.class))).thenThrow(new DataIntegrityViolationException("Duplicate production slug"));
            // Act & Assert
            DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {
                productionService.updateProduction(1, testUpdateProductionRequest);
            });
        }

    }
}
