package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

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

        private PersonRequest personRequest;
        private Person person;

        @BeforeEach
        void setup() {
            personRequest = new PersonRequest(
                    "Test First Name",
                    "Test Last Name",
                    "Test Summary",
                    "01111 111111",
                    "07111 111111",
                    "Test Street",
                    "Test Town",
                    "Test Postcode"
            );
            person = new Person(
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
            personService.addNewPerson(personRequest);
            // Assert
            verify(personRepository, times(1)).save(any(Person.class));
        }

        @Test
        void testSaveDataIntegrityViolationThrowsDatabaseException() {
            // Arrange
            when(personRepository.save(any(Person.class))).thenThrow(new DataIntegrityViolationException("Data integrity violation"));
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                personService.addNewPerson(personRequest);
            });
        }

    }
}
