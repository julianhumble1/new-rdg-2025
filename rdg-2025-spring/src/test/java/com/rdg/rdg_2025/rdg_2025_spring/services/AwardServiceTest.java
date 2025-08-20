package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.award.AwardRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.AwardRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AwardServiceTest {

    @Mock
    private AwardRepository awardRepository;

    @Mock
    private FestivalService festivalService;

    @Mock
    private PersonService personService;

    @Mock
    private ProductionService productionService;

    @InjectMocks
    private AwardService awardService;

    private Festival festival;
    private Person person;
    private Production production;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.openMocks(this);
        festival = new Festival();
        festival.setId(1);
        person = new Person();
        person.setId(2);
        production = new Production();
        production.setId(3);
    }

    @Nested
    @DisplayName("addNewAward service tests")
    class AddNewAwardServiceTests {

        private AwardRequest request;

        @BeforeEach
        void setup() {
            request = new AwardRequest("Best Sound", 3, 2, 1);
        }

        @Test
        void testFestivalServiceIsCalled() {
            awardService.addNewAward(request);
            verify(festivalService, times(1)).getFestivalById(anyInt());
        }

        @Test
        void testWhenFestivalMissingThrowsEntityNotFound() {
            when(festivalService.getFestivalById(1)).thenThrow(new EntityNotFoundException(""));
            assertThrows(EntityNotFoundException.class, () -> awardService.addNewAward(request));
            verify(awardRepository, never()).save(any());
        }

        @Test
        void testWhenFestivalDbErrorThrowsDatabaseException() {
            when(festivalService.getFestivalById(1)).thenThrow(new DatabaseException(""));
            assertThrows(DatabaseException.class, () -> awardService.addNewAward(request));
            verify(awardRepository, never()).save(any());
        }

        @Test
        void testIfNoPersonIdProvidedThenPersonServiceNotCalled() {
            request.setPersonId(0);
            awardService.addNewAward(request);
            verify(personService, never()).getPersonById(anyInt());
        }

        @Test
        void testIfPersonIdProvidedThenPersonServiceCalled() {
            awardService.addNewAward(request);
            verify(personService, times(1)).getPersonById(anyInt());
        }

        @Test
        void testRepositorySaveIsCalledAndReturnedObjectIsPropagated() {
            when(festivalService.getFestivalById(1)).thenReturn(festival);
            when(personService.getPersonById(2)).thenReturn(person);
            when(productionService.getProductionById(3)).thenReturn(production);

            Award saved = new Award(10, "Best Sound", festival, person, production);
            when(awardRepository.save(any(Award.class))).thenReturn(saved);

            Award result = awardService.addNewAward(request);

            assertEquals(saved, result);
            verify(awardRepository, times(1)).save(any(Award.class));
        }

        @Test
        void testSaveThrowsDataAccessWrappedAsDatabaseException() {
            when(festivalService.getFestivalById(1)).thenReturn(festival);
            when(personService.getPersonById(2)).thenReturn(person);
            when(productionService.getProductionById(3)).thenReturn(production);

            DataAccessException dae = new DataAccessException("db") {
            };
            when(awardRepository.save(any(Award.class))).thenThrow(dae);

            assertThrows(DatabaseException.class, () -> awardService.addNewAward(request));
        }
    }

    @Nested
    @DisplayName("getAwardById service tests")
    class GetAwardByIdServiceTests {

        @Test
        void testRepositoryFindByIdCalledAndReturned() {
            Award a = new Award(5, "Name", null, null, null);
            when(awardRepository.findById(5)).thenReturn(Optional.of(a));

            Award result = awardService.getAwardById(5);

            assertEquals(a, result);
            verify(awardRepository, times(1)).findById(5);
        }

        @Test
        void testNotFoundThrowsEntityNotFoundException() {
            when(awardRepository.findById(42)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> awardService.getAwardById(42));
        }

        @Test
        void testDataAccessExceptionWrappedAsDatabaseException() {
            when(awardRepository.findById(anyInt())).thenThrow(new DataAccessException("") {
            });
            assertThrows(DatabaseException.class, () -> awardService.getAwardById(1));
        }
    }

    @Nested
    @DisplayName("updateAward service tests")
    class UpdateAwardServiceTests {

        private AwardRequest request;
        private Award existing;

        @BeforeEach
        void setup() {
            request = new AwardRequest("Updated Award", 1, 2, 3);
            existing = new Award(7, "Old", null, null, null);
        }

        @Test
        void testFindAndUpdateFlow() {
            when(awardRepository.findById(7)).thenReturn(Optional.of(existing));
            when(festivalService.getFestivalById(3)).thenReturn(festival);
            when(personService.getPersonById(2)).thenReturn(person);
            when(productionService.getProductionById(1)).thenReturn(production);
            when(awardRepository.save(existing)).thenReturn(existing);

            Award result = awardService.updateAward(7, request);

            assertEquals(existing, result);
            assertEquals("Updated Award", existing.getName());
            assertEquals(festival, existing.getFestival());
            assertEquals(person, existing.getPerson());
            assertEquals(production, existing.getProduction());
            verify(awardRepository).save(existing);
        }

        @Test
        void testFindThrowsDataAccessWrappedAsDatabaseException() {
            when(awardRepository.findById(7)).thenThrow(new DataAccessException("") {
            });
            assertThrows(DatabaseException.class, () -> awardService.updateAward(7, request));
        }

        @Test
        void testNotFoundThrowsEntityNotFound() {
            when(awardRepository.findById(7)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> awardService.updateAward(7, request));
        }

        @Test
        void testSaveThrowsPersistenceWrappedAsDatabaseException() {
            // make stubbings lenient to avoid UnnecessaryStubbingException under Mockito
            // strictness
            lenient().when(awardRepository.findById(7)).thenReturn(Optional.of(existing));
            lenient().when(festivalService.getFestivalById(1)).thenReturn(festival);
            lenient().when(personService.getPersonById(2)).thenReturn(person);
            lenient().when(productionService.getProductionById(3)).thenReturn(production);
            lenient().when(awardRepository.save(existing)).thenThrow(new PersistenceException(""));

            assertThrows(DatabaseException.class, () -> awardService.updateAward(7, request));
        }
    }

    @Nested
    @DisplayName("deleteAwardById service tests")
    class DeleteAwardByIdServiceTests {

        @Test
        void testDeleteFlowCallsRepositoryDelete() {
            Award existing = new Award(8, "Name", null, null, null);
            when(awardRepository.findById(8)).thenReturn(Optional.of(existing));

            awardService.deleteAwardById(8);

            verify(awardRepository, times(1)).delete(existing);
        }

        @Test
        void testFindThrowsDataAccessWrappedAsDatabaseException() {
            when(awardRepository.findById(anyInt())).thenThrow(new DataAccessException("") {
            });
            assertThrows(DatabaseException.class, () -> awardService.deleteAwardById(1));
        }

        @Test
        void testNotFoundThrowsEntityNotFound() {
            when(awardRepository.findById(anyInt())).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> awardService.deleteAwardById(1));
        }

        @Test
        void testDeleteThrowsPersistenceWrappedAsDatabaseException() {
            Award existing = new Award(9, "Name", null, null, null);
            when(awardRepository.findById(9)).thenReturn(Optional.of(existing));
            doThrow(new PersistenceException("")).when(awardRepository).delete(any());

            assertThrows(DatabaseException.class, () -> awardService.deleteAwardById(9));
        }
    }

}