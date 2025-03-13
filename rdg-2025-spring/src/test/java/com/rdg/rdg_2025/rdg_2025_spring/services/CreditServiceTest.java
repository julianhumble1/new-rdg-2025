package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.credit.CreditRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.CreditRepository;
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
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private ProductionService productionService;

    @Mock
    private PersonService personService;

    @InjectMocks
    private CreditService creditService;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("addNewCredit service tests")
    class AddNewCreditServiceTests {

        private CreditRequest testCreditRequest;

        @BeforeEach
        void setup() {
            testCreditRequest = new CreditRequest(
                    "Test Credit",
                    CreditType.ACTOR,
                    1,
                    1,
                    "Test Summary"
            );
        }

        @Test
        void testProductionServiceMethodIsCalled() {
            // Arrange
            // Act
            creditService.addNewCredit(testCreditRequest);
            // Assert
            verify(productionService, times(1)).getProductionById(anyInt());
        }

        @Test
        void testProductionDoesNotExistThrowsEntityNotFoundException() {
            // Arrange
            when(productionService.getProductionById(1)).thenThrow(new EntityNotFoundException(""));
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                creditService.addNewCredit(testCreditRequest);
            });
        }

        @Test
        void testRetrieveProductionDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(productionService.getProductionById(1)).thenThrow(new DatabaseException(""));
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                creditService.addNewCredit(testCreditRequest);
            });
        }

        @Test
        void testIfNoPersonIdProvidedThenPersonServiceNotCalled() {
            // Arrange
            testCreditRequest.setPersonId(0);
            // Act
            creditService.addNewCredit(testCreditRequest);
            // Assert
            verify(personService, never()).getPersonById(anyInt());
        }

        @Test
        void testIfPersonIdProvidedThenPersonServiceCalled() {
            // Arrange
            // Act
            creditService.addNewCredit(testCreditRequest);
            // Assert
            verify(personService, times(1)).getPersonById(anyInt());
        }

        @Test
        void testRetrievePersonDatabaseExceptionThrowsDatabaseException() {
            // Arrange
            when(productionService.getProductionById(anyInt())).thenThrow(new DatabaseException(""));
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                creditService.addNewCredit(testCreditRequest);
            });
        }

        @Test
        void testPersonDoesNotExistThrowsEntityNotFoundException() {
            // Arrange
            when(personService.getPersonById(1)).thenThrow(new EntityNotFoundException(""));
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                creditService.addNewCredit(testCreditRequest);
            });
        }

        @Test
        void testCreditRepositorySaveMethodIsCalled() {
            // Arrange
            // Act
            creditService.addNewCredit(testCreditRequest);
            // Assert
            verify(creditRepository, times(1)).save(any());
        }

        @Test
        void testReturnsExpectedCreditObject() {
            // Arrange
            Credit testCredit = new Credit(
                    "Test Credit",
                    CreditType.ACTOR,
                    new Person(),
                    new Production(),
                    "Test Summary"
            );
            when(creditRepository.save(any())).thenReturn(testCredit);
            // Act
            Credit result = creditService.addNewCredit(testCreditRequest);
            // Assert
            assertEquals("Test Credit", result.getName());
        }
    }

    @Nested
    @DisplayName("getCreditById service tests")
    class GetCreditByIdServiceTests {

        private Credit testCredit;

        @BeforeEach
        void setup() {
            testCredit = new Credit(
                    "Test Credit",
                    CreditType.ACTOR,
                    new Person(),
                    new Production(),
                    "Test Summary"
            );
        }

        @Test
        void testRepositoryFindByIdMethodIsCalled() {
            // Arrange
            when(creditRepository.findById(anyInt())).thenReturn(Optional.of(testCredit));
            // Act
            creditService.getCreditById(1);
            // Assert
            verify(creditRepository, times(1)).findById(1);

        }

        @Test
        void testNonExistentCreditThrowsEntityNotFoundException() {
            // Arrange
            when(creditRepository.findById(anyInt())).thenReturn(Optional.empty());
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                creditService.getCreditById(1);
            });

        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(creditRepository.findById(anyInt())).thenThrow(new DataAccessException("") {});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                creditService.getCreditById(1);
            });

        }


    }
}
