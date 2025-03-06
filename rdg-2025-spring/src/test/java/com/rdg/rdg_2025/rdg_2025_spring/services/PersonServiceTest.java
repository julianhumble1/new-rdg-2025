package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    }
}
