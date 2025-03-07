package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.VenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityExistsException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    @InjectMocks
    private VenueService venueService;

    @Mock
    private ProductionService productionService;

    @Mock
    private FestivalService festivalService;

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

        Venue testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

        @Test
        void testSuccessfulDeletionDoesNotThrowAnException() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            // Act & Assert
            assertDoesNotThrow(() -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testGetDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findById(any())).thenThrow(new DataAccessException("Data Access Exception"){});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testDeleteDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            doThrow(new DataAccessException("Data access exception") {}).when(venueRepository).delete(any());
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testDeletePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            doThrow(new PersistenceException("Data persistence exception") {}).when(venueRepository).delete(any());
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testNonExistentVenueIdThrowsEntityNotFoundException() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenThrow(new EntityNotFoundException("no venue with this id"));
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                venueService.deleteVenueById(1);
            });

        }

        @Test
        void testVenueHasNoProductionsThenProductionServiceIsNotCalled() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            // Act
            venueService.deleteVenueById(1);
            // Assert
            verify(productionService, never()).setProductionVenueFieldToNull(any());
        }

        @Test
        void testIfVenueHasOneProductionThenProductionServiceIsCalledOnce() {
            // Arrange
            Production production = new Production();
            List<Production> productionList = new ArrayList<>();
            productionList.add(production);
            testVenue.setProductions(productionList);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            // Act
            venueService.deleteVenueById(1);
            // Assert
            verify(productionService, times(1)).setProductionVenueFieldToNull(any());
        }

        @Test
        void testProductionServiceEntityNotFoundThrowsEntityNotFoundException() {
            // Arrange
            Production production = new Production();
            List<Production> productionList = new ArrayList<>();
            productionList.add(production);
            testVenue.setProductions(productionList);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            doThrow(DataIntegrityViolationException.class).when(productionService).setProductionVenueFieldToNull(any());
            // Act & Assert
            assertThrows(DataIntegrityViolationException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testIfVenueHasMultipleProductionsThenProductionServiceIsCalledMultipleTimes() {
            // Arrange
            Production production1 = new Production();
            Production production2 = new Production();
            List<Production> productionList = new ArrayList<>();
            productionList.add(production1);
            productionList.add(production2);
            testVenue.setProductions(productionList);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            // Act
            venueService.deleteVenueById(1);
            // Assert
            verify(productionService, times(2)).setProductionVenueFieldToNull(any());
        }

        @Test
        void testIfProductionServiceThrowsDatabaseExceptionThenThrowsDatabaseException() {
            // Arrange
            Production production = new Production();
            List<Production> productionList = new ArrayList<>();
            productionList.add(production);
            testVenue.setProductions(productionList);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            doThrow(new DatabaseException("database exception")).when(productionService).setProductionVenueFieldToNull(any());

            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });
        }

        @Test
        void testIfVenueHasNoFestivalsThenFestivalServiceNotCalled() {
            // Arrange
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            // Act
            venueService.deleteVenueById(1);
            // Assert
            verify(festivalService, never()).setFestivalVenueFieldToNull(any());
        }

        @Test
        void testIfVenueHasOneFestivalThenServiceIsCalledOnce() {
            // Arrange
            Festival festival = new Festival();
            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(festival);
            testVenue.setFestivals(festivalList);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));
            // Act
            venueService.deleteVenueById(1);
            // Assert
            verify(festivalService, times(1)).setFestivalVenueFieldToNull(any());

        }

        @Test
        void testIfVenueHasMultipleFestivalsThenServiceIsCalledMultipleTimes() {
            // Arrange
            Festival festival1 = new Festival();
            Festival festival2 = new Festival();
            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(festival1);
            festivalList.add(festival2);
            testVenue.setFestivals(festivalList);
            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));

            // Act
            venueService.deleteVenueById(1);
            // Assert
            verify(festivalService, times(2)).setFestivalVenueFieldToNull(any());

        }

        @Test
        void testFestivalServiceThrowsDatabaseExceptionThenThrowsDatabaseException() {
            // Arrange
            Festival festival = new Festival();
            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(festival);
            testVenue.setFestivals(festivalList);

            when(venueRepository.findById(anyInt())).thenReturn(Optional.of(testVenue));

            doThrow(new DatabaseException("database exception")).when(festivalService).setFestivalVenueFieldToNull(any());

            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                venueService.deleteVenueById(1);
            });

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

    @Nested
    @DisplayName("removeFestivalFromVenueList service tests")
    class RemoveFestivalFromVenueListServiceTests {

        private Venue testVenue;
        private Festival testFestival;

        @BeforeEach
        void beforeEach() {
            testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");

            testFestival = new Festival(
                    "Test Festival",
                    testVenue,
                    2025,
                    1,
                    "Test Description"
            );
            List<Festival> festivalList = new ArrayList<>();
            festivalList.add(testFestival);
            testVenue.setFestivals(festivalList);
        }

        @Test
        void testIfFestivalHasNoAssociatedVenueThenSaveIsNotCalled() {
            // Arrange
            Festival noVenueFestival = new Festival();
            // Act
            venueService.removeFestivalFromVenueFestivalList(noVenueFestival);
            // Assert
            verify(venueRepository, never()).save(any());
        }

        @Test
        void testFestivalListReducesLengthByOneAfterMethodCall() {
            // Arrange
            int festivalListStartingLength = testVenue.getFestivals().size();
            when(venueRepository.save(any())).thenReturn(testVenue);
            // Act
            venueService.removeFestivalFromVenueFestivalList(testFestival);
            // Assert
            assertEquals(festivalListStartingLength - 1, testVenue.getFestivals().size());
        }

        @Test
        void testUpdatedVenueIsSavedToDatabase() {
            // Arrange
            when(venueRepository.save(any())).thenReturn(testVenue);

            // Act
            venueService.removeFestivalFromVenueFestivalList(testFestival);
            // Assert
            verify(venueRepository, atLeastOnce()).save(any());
        }

    }

    @Nested
    @DisplayName("removePerformanceFromVenueList service tests")
    class RemovePerformanceFromVenueListServiceTests {

        private Performance testPerformance;
        private Venue testVenue;

        @BeforeEach
        void setUp() {
            testVenue = new Venue("Test Venue", "Test Notes", "Test Postcode", "Test Address", "Test Town", "www.test.com");
            testPerformance = new Performance();
            testPerformance.setVenue(testVenue);
            ArrayList<Performance> performances= new ArrayList<>();
            performances.add(testPerformance);
            testVenue.setPerformances(performances);
        }

        @Test
        void testPerformanceListLengthReducesByOne() {
            // Arrange
            int initialLength = testVenue.getPerformances().size();
            // Act
            venueService.removePerformanceFromVenuePerformanceList(testPerformance);
            // Assert
            assertEquals(initialLength - 1, testVenue.getPerformances().size());
        }

        @Test
        void testUpdatedVenueIsSavedToDatabase() {
            // Arrange
            // Act
            venueService.removePerformanceFromVenuePerformanceList(testPerformance);
            // Assert
            verify(venueRepository, times(1)).save(testVenue);
        }

    }
}
