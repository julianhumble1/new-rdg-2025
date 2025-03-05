package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    // CRUD METHODS

    public Person addNewPerson(PersonRequest personRequest) {

        Person person = new Person();
        updatePersonFromRequest(personRequest, person);

        try {
            return personRepository.save(person);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage(), ex);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }

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

        person.setSlug(SlugUtils.generateSlug(person.getFirstName(), person.getLastName()));

        if (person.getCreatedAt() == null) {
            person.setCreatedAt(LocalDateTime.now());
        }
        person.setUpdatedAt(LocalDateTime.now());
    }

}
