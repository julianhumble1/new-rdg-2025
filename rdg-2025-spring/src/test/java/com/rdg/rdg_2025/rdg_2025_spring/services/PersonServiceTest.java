package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Nested
    @DisplayName("addNewPerson service tests")
    class AddNewPersonServiceTests {

        private PersonRequest testPersonRequest;
        private Person testPerson;

        @BeforeEach
        void setup() {
            testPersonRequest = new PersonRequest(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            testPerson = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
        }

        @Test
        void testSavePersonIsCalledOnPersonRepository() {
            // Arrange
            // Act
            personService.addNewPerson(testPersonRequest);
            // Assert
            verify(personRepository, times(1)).save(any(Person.class));
        }

        @Test
        void testSaveDataIntegrityViolationThrowsDataIntegrityViolationException() {
            // Arrange
            when(personRepository.save(any(Person.class))).thenThrow(new DataIntegrityViolationException("Data integrity violation"));
            // Act & Assert
            assertThrows(DataIntegrityViolationException.class, () -> {
                personService.addNewPerson(testPersonRequest);
            });
        }

        @Test
        void testSaveDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.save(any(Person.class))).thenThrow(new DataAccessException("Data access exception") {});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.addNewPerson(testPersonRequest);
            });
        }

        @Test
        void testSaveDataPersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.save(any(Person.class))).thenThrow(new PersistenceException("Persistence exception") );
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.addNewPerson(testPersonRequest);
            });
        }

        @Test
        void testSuccessfulSaveReturnsPerson() {
            // Arrange
            when(personRepository.save(any(Person.class))).thenReturn(testPerson);
            // Act
            Person actual = personService.addNewPerson(testPersonRequest);
            // Assert
            assertEquals(testPerson, actual);
        }

    }

    @Nested
    @DisplayName("getAllPeople service tests")
    class GetAllPeopleServiceTests {

        @Test
        void testRepositoryFindAllMethodIsCalled() {
            // Arrange
            // Act
            personService.getAllPeople();
            // Assert
            verify(personRepository, times(1)).findAll();
        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.findAll()).thenThrow(new DataAccessException("Data access exception")  {});
            // Act & Assert
            assertThrows(DatabaseException.class, () ->{
                personService.getAllPeople();
            });
        }

        @Test
        void testEmptyDatabaseReturnsEmptyList() {
            // Arrange
            when(personRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
            // Act
            List<Person> people = personService.getAllPeople();
            // Assert
            assertEquals(0, people.size());
        }

        @Test
        void testOnePersonInDatabaseReturnsListLengthOne() {
            // Arrange
            Person testPerson1 = new Person();
            ArrayList<Person> testPersonList = new ArrayList<>();
            testPersonList.add(testPerson1);
            when(personRepository.findAll()).thenReturn(testPersonList);
            // Act
            List<Person> people = personService.getAllPeople();
            // Assert
            assertEquals(1, people.size());
        }

        @Test
        void testMultiplePeopleInDatabaseReturnsExpectedListLength() {
            // Arrange
            Person testPerson1 = new Person();
            Person testPerson2 = new Person();
            ArrayList<Person> testPersonList = new ArrayList<>();
            testPersonList.add(testPerson1);
            testPersonList.add(testPerson2);
            when(personRepository.findAll()).thenReturn(testPersonList);
            // Act
            List<Person> people = personService.getAllPeople();
            // Assert
            assertEquals(2, people.size());
        }

    }

    @Nested
    @DisplayName("deletePersonById service tests")
    class DeletePersonByIdServiceTests {

        private Person testPerson;

        @BeforeEach
        void setup() {
            testPerson = new Person();
        }

        @Test
        void testRepositoryFindPersonMethodIsCalled() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            // Act
            personService.deletePersonById(1);
            // Assert
            verify(personRepository, times(1)).findById(anyInt());

        }

        @Test
        void testFindDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenThrow(new DataAccessException("data access failed"){});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.deletePersonById(1);
            });

        }

        @Test
        void testFindEntityNotFoundThrowsEntityNotFoundException() {
            // Arrange
            doThrow(new EntityNotFoundException("person not found")).when(personRepository).findById(anyInt());
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                personService.deletePersonById(1);
            });

        }

        @Test
        void testRepositoryDeleteMethodIsCalled() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            // Act
            personService.deletePersonById(1);
            // Assert
            verify(personRepository, times(1)).delete(any(Person.class));

        }

        @Test
        void testDeleteDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            doThrow(new DataAccessException("data access failed") {}).when(personRepository).delete(any(Person.class));
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.deletePersonById(1);
            });

        }

        @Test
        void testDeletePersistenceExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            doThrow(new PersistenceException("persistence failed")).when(personRepository).delete(any(Person.class));
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.deletePersonById(1);
            });

        }


    }

    @Nested
    @DisplayName("getPersonById service tests")
    class GetPersonByIdServiceTests {

        @Test
        void testRepositoryFindByIdMethodCalled() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(new Person()));
            // Act
            personService.getPersonById(1);
            // Assert
            verify(personRepository, times(1)).findById(anyInt());
        }

        @Test
        void testDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenThrow(new DataAccessException("data access exception") {});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.getPersonById(1);
            });
        }

        @Test
        void testInvalidIdThrowsEntityNotFoundException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.empty());
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                personService.getPersonById(1);
            });
        }

        @Test
        void testSuccessfulGetReturnsExpectedPersonObject() {
            // Arrange
            Person testPerson = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            // Act
            Person result = personService.getPersonById(1);
            // Assert
            assertEquals(testPerson, result);

        }


    }

    @Nested
    @DisplayName("updatePerson service tests")
    class UpdatePersonServiceTests {

        private PersonRequest testUpdatePersonRequest;
        private Person testPerson;

        @BeforeEach
        void setup() {
            testUpdatePersonRequest = new PersonRequest(
                    "Updated First Name",
                    "Updated Last Name",
                    "Updated Summary",
                    "01222 222222",
                    "07222 222222",
                    "Updated Street",
                    "Updated Town",
                    "Updated Postcode"
            );
            testPerson = new Person(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
        }

        @Test
        void testRepositoryFindMethodIsCalled() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            // Act
            personService.updatePerson(1, testUpdatePersonRequest);
            // Assert
            verify(personRepository, times(1)).findById(1);
        }

        @Test
        void testFindDataAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenThrow(new DataAccessException("data access failed") {});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.updatePerson(1, testUpdatePersonRequest);
            });
        }

        @Test
        void testInvalidPersonIdThrowsEntityNotFoundException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.empty());
            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> {
                personService.updatePerson(1, testUpdatePersonRequest);
            });
        }

        @Test
        void testRepositorySaveMethodIsCalled() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            // Act
            personService.updatePerson(1, testUpdatePersonRequest);
            // Assert
            verify(personRepository, times(1)).save(any());
        }

        @Test
        void testDataIntegrityViolationThrowsDataIntegrityViolationException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            when(personRepository.save(any(Person.class))).thenThrow(new DataIntegrityViolationException(""));
            // Act & Assert
            assertThrows(DataIntegrityViolationException.class, () -> {
                personService.updatePerson(1, testUpdatePersonRequest);
            });
        }

        @Test
        void testSaveAccessExceptionThrowsDatabaseException() {
            // Arrange
            when(personRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
            when(personRepository.save(any(Person.class))).thenThrow(new DataAccessException("") {});
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.updatePerson(1, testUpdatePersonRequest);
            });
        }

    }
}
