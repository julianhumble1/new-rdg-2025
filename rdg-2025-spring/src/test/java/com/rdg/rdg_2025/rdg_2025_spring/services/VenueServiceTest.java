package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.VenueRequest;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    @InjectMocks
    private VenueService venueService;

    @Mock
    private ProductionService productions;

    @Mock
    private VenueRepository venueRepository;

    @Nested
    @DisplayName("addNewVenue service tests")
    class AddNewVenueServiceTests {

        @Test
        void testAddNewVenueWithAllFieldsSuccessReturnsExpectedVenueObject() {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueRepository.save(any(Venue.class))).thenReturn(testVenue);

            // Act
            Venue result = venueService.addNewVenue(
                    new VenueRequest("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com")
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
                    new VenueRequest("Test Venue", null, null, null, null, null)
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
                venueService.addNewVenue(new VenueRequest(
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
                venueService.addNewVenue(new VenueRequest(
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
                venueService.addNewVenue(new VenueRequest(
                        "Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com")
                );
            });
        }

    }

    @Nested
    @DisplayName("getAllVenues service tests")
    class GetAllVenuesServiceTests {

        @Test
        void testGetAllVenuesWithEmptyDatabaseReturnsEmptyList() {
            // Arrange
            when(venueRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

            // Act
            List<Venue> venues = venueService.getAllVenues();
            // Assert
            assertEquals(Collections.EMPTY_LIST, venues);
        }

        @Test
        void testGetAllVenuesWithOneVenueInDatabaseReturnsListLengthOne() {
            // Arrange
            Venue testVenue1 = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            ArrayList<Venue> venueList = new ArrayList<>();
            venueList.add(testVenue1);
            when(venueRepository.findAll()).thenReturn(venueList);

            // Act
            List<Venue> result = venueService.getAllVenues();
            // Assert
            assertEquals(1, result.size());
        }

        @Test
        void testGetAllVenuesWithOneVenueInDatabaseReturnsExpectedVenue() {
            // Arrange
            Venue testVenue1 = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            ArrayList<Venue> venueList = new ArrayList<>();
            venueList.add(testVenue1);
            when(venueRepository.findAll()).thenReturn(venueList);

            // Act
            List<Venue> result = venueService.getAllVenues();
            // Assert
            assertEquals(testVenue1.getId(), result.get(0).getId());
        }

        @Test
        void testGetAllVenuesWithMultipleVenuesInDatabaseReturnsExpectedLength() {
            // Arrange
            Venue testVenue1 = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            Venue testVenue2 = new Venue("Another Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

            ArrayList<Venue> venueList = new ArrayList<>();
            venueList.add(testVenue1);
            venueList.add(testVenue2);
            when(venueRepository.findAll()).thenReturn(venueList);

            // Act
            List<Venue> result = venueService.getAllVenues();
            // Assert
            assertEquals(venueList.size(), 2);
        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findAll()).thenThrow(new DataAccessException("Data access failed"){});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.getAllVenues();
            });
        }

        @Test
        void testPersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findAll()).thenThrow(new PersistenceException("Data access failed"){});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.getAllVenues();
            });
        }


    }

    @Nested
    @DisplayName("deleteVenue service tests")
    class DeleteVenueServiceTests {

        Venue testVenue1 = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
        Venue testVenue2 = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

        @Test
        void testSuccessfulDeletionReturnsTrue() {
            // Arrange
            when(venueRepository.existsById(any())).thenReturn(true);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue1));
            // Act
            boolean result = venueService.deleteVenueById(1);
            // Assert
            assertEquals(true, result);
        }

        @Test
        void testExistsDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.existsById(any())).thenThrow(new DataAccessException("Data Access Exception"){});
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testDeleteDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.existsById(any())).thenReturn(true);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue1));

            doThrow(new DataAccessException("Data access exception") {}).when(venueRepository).deleteById(anyInt());
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testDeletePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.existsById(any())).thenReturn(true);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue1));

            doThrow(new PersistenceException("Data persistence exception") {}).when(venueRepository).deleteById(anyInt());
            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testNonExistentVenueIdReturnsFalse() {
            // Arrange
            when(venueRepository.existsById(anyInt())).thenReturn(false);
            // Act
            boolean result = venueService.deleteVenueById(1);
            // Assert
            assertEquals(false, result);

        }
    }

    @Nested
    @DisplayName("getVenueById service tests")
    class GetVenueByIdServiceTests {

        @Test
        void testReturnsFoundVenueWhenVenueExists() {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));

            // Act
            Venue fetchedVenue = venueService.getVenueById(testVenue.getId());
            // Assert
            assertEquals(testVenue, fetchedVenue);
        }

        @Test
        void testThrowsEntityNotFoundExceptionWhenVenueDoesNotExist() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                venueService.getVenueById(1);
            });
        }

        @Test
        void testThrowsDatabaseExceptionWhenDataAccessException() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenThrow(new DataAccessException("Data access exception") {});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.getVenueById(1);
            });
        }

    }

    @Nested
    @DisplayName("updateVenue service tests")
    class UpdateVenueServiceTests {

        @Test
        void updateVenueWithAllFieldsSuccessReturnsExpectedVenueObject() {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));

            VenueRequest updateVenueRequest = new VenueRequest(
                    "Updated Test Venue",
                    "Updated Test Notes",
                    "Updated Test Postcode",
                    "Updated Test Address",
                    "Updated Test Town",
                    "www.updatedtest.com"
                    );

            Venue updatedTestVenue = new Venue(
                    "Updated Test Venue",
                    "Updated Test Notes",
                    "Updated Test Postcode",
                    "Updated Test Address",
                    "Updated Test Town",
                    "www.updatedtest.com"
            );

            when(venueRepository.save(any(Venue.class))).thenReturn(updatedTestVenue);

            // Act
            Venue actualVenue = venueService.updateVenue(1, updateVenueRequest);
            // Assert
            assertEquals("Updated Test Venue", actualVenue.getName());
            assertEquals("Updated Test Notes", actualVenue.getNotes());
            assertEquals("Updated Test Postcode", actualVenue.getPostcode());
            assertEquals("Updated Test Address", actualVenue.getAddress());
            assertEquals("Updated Test Town", actualVenue.getTown());
            assertEquals("www.updatedtest.com", actualVenue.getUrl());
            assertEquals("updated-test-venue", actualVenue.getSlug());
        }

        @Test
        void testFindDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenThrow(new DataAccessException("Data access exception") {});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
               venueService.updateVenue(1, new VenueRequest());
            });
        }

        @Test
        void testSaveDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));

            when(venueRepository.save(any(Venue.class))).thenThrow(new DataAccessException("Data access exception") {});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.updateVenue(1, new VenueRequest());
            });

        }

        @Test
        void testSavePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));

            when(venueRepository.save(any(Venue.class))).thenThrow(new PersistenceException("Persistence exception") {});

            // Act & Assert
            DatabaseException ex = assertThrows(DatabaseException.class, () -> {
                venueService.updateVenue(1, new VenueRequest());
            });

        }

        @Test
        void testSameNameAsExistingVenueThrowsDataIntegrityViolationException() {
            // Arrange
            Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));

            when(venueRepository.save(any(Venue.class))).thenThrow(new DataIntegrityViolationException("Data Integrity Violation"));

            // Act & Assert
            DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {
                venueService.updateVenue(1, new VenueRequest());
            });

        }

        @Test
        void testVenueDoesNotExistsThrowsEntityNotFoundException() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                venueService.updateVenue(1, new VenueRequest());
            });
        }


    }

    @Nested
    @DisplayName("removeProductionFromVenueList service tests")
    class RemoveProductionFromVenueListServiceTests {

        private Venue testVenue;
        private Production testProduction;

        @BeforeEach
        void beforeEach() {
            testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

            testProduction = new Production(
                    "Test Production",
                    testVenue,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test File String"
            );
            List<Production> productions = new ArrayList<>();
            productions.add(testProduction);
            testVenue.setProductions(productions);
        }

        @Test
        void testIfProductionHasNoAssociatedVenueSaveIsNotCalled() {
            // Arrange
            Production noVenueProduction = new Production();
            // Act
            venueService.removeProductionFromVenueProductionList(noVenueProduction);
            // Assert
            verify(venueRepository, never()).save(any(Venue.class));
        }

        @Test
        void testProductionListReducesLengthByOneAfterMethodCall() {
            // Arrange
            int productionListStartingLength = testVenue.getProductions().size();
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            when(venueRepository.save(any())).thenReturn(testVenue);
            // Act
            venueService.removeProductionFromVenueProductionList(testProduction);
            // Assert
            assertEquals(productionListStartingLength -1, testVenue.getProductions().size());
        }

        @Test
        void testUpdatedVenueIsSavedToDatabase() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            when(venueRepository.save(any())).thenReturn(testVenue);
            // Act
            venueService.removeProductionFromVenueProductionList(testProduction);
            // Assert
            verify(venueRepository, atLeastOnce()).save(any());
        }

    }
}
