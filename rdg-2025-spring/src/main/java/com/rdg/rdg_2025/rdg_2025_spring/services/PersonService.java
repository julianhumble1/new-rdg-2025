package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    private CreditService creditService;

    @Autowired
    public PersonService(@Lazy  CreditService creditService) {
        this.creditService = creditService;
    }

    // CRUD METHODS

    public Person addNewPerson(PersonRequest personRequest) {

        Person person = new Person();
        updatePersonFromRequest(personRequest, person);

        return savePersonToDatabase(person);
    }

    public List<Person> getAllPeople() {
        try {
            return personRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    public Person getPersonById(int personId) {
        try {
            return personRepository.findById(personId)
                    .orElseThrow(() -> new EntityNotFoundException("No Person with this id: " + personId));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    public void deletePersonById(int personId) {
        Person person = getPersonById(personId);
        setAssociatedCreditsPersonToNull(person);
        deletePersonInDatabase(person);
    }

    public Person updatePerson(int personId, PersonRequest personRequest) {
        Person person = getPersonById(personId);
        updatePersonFromRequest(personRequest, person);
        return savePersonToDatabase(person);
    }

    // PRIVATE HELPER METHODS

    private void updatePersonFromRequest(PersonRequest personRequest, Person person) {
        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setSummary(personRequest.getSummary());
        person.setHomePhone(personRequest.getHomePhone());
        person.setMobilePhone(personRequest.getMobilePhone());
        person.setAddressStreet(personRequest.getAddressStreet());
        person.setAddressTown(personRequest.getAddressTown());
        person.setAddressPostcode(personRequest.getAddressPostcode());
        person.setImageId(personRequest.getImageId());

        person.setSlug(SlugUtils.generateSlug(person.getFirstName(), person.getLastName()));

        if (person.getCreatedAt() == null) {
            person.setCreatedAt(LocalDateTime.now());
        }
        person.setUpdatedAt(LocalDateTime.now());
    }

    private Person savePersonToDatabase(Person person) {
        try {
            return personRepository.save(person);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage(), ex);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    private void deletePersonInDatabase(Person person) {
        try {
            personRepository.delete(person);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    private void setAssociatedCreditsPersonToNull(Person person) {
        List<Credit> creditList = person.getCredits();
        creditList.forEach((credit -> {
            creditService.setAssociatedPersonToNull(credit);
        }));
    }

}
