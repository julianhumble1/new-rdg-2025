package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    // CRUD METHODS

    public Person addNewPerson(PersonRequest personRequest) {

        Person person = new Person();

        return personRepository.save(person);

    }

}
