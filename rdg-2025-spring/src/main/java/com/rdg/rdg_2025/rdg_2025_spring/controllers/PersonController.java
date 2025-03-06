package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.person.PersonResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonService personService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewVenue(@Valid @RequestBody PersonRequest personRequest) {

        try {
            Person person = personService.addNewPerson(personRequest);
            URI location = URI.create("/people/" + person.getId());
            return ResponseEntity.created(location).body(new PersonResponse(person));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllPeople() {
        personService.getAllPeople();
        return ResponseEntity.ok().build();
    }
}
