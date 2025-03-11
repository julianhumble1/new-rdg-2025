package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.person.DetailedPeopleResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.person.DetailedPersonResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.person.PublicPeopleResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.person.PublicPersonResponse;
import com.rdg.rdg_2025.rdg_2025_spring.security.jwt.JwtUtils;
import com.rdg.rdg_2025.rdg_2025_spring.services.PersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewVenue(@Valid @RequestBody PersonRequest personRequest) {
        try {
            Person person = personService.addNewPerson(personRequest);
            URI location = URI.create("/people/" + person.getId());
            return ResponseEntity.created(location).body(new DetailedPersonResponse(person));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllPeople() {
        try {
            List<Person> personList = personService.getAllPeople();
            if (jwtUtils.checkAdmin()) {
                return ResponseEntity.ok().body(new DetailedPeopleResponse(personList));
            } else {
                return ResponseEntity.ok().body(new PublicPeopleResponse(personList));
            }
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{personId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePersonById(@PathVariable int personId) {

        try {
            personService.deletePersonById(personId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @GetMapping("/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable int personId) {

        try {
            Person person = personService.getPersonById(personId);
            if (jwtUtils.checkAdmin()) {
                return ResponseEntity.ok(new DetailedPersonResponse(person));
            } else {
                return ResponseEntity.ok(new PublicPersonResponse(person));
            }
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/{personId}")
    public ResponseEntity<?> updatePerson(@PathVariable int personId, @Valid @RequestBody PersonRequest personRequest) {

        try {
            personService.updatePerson(personId, personRequest);
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }


}
