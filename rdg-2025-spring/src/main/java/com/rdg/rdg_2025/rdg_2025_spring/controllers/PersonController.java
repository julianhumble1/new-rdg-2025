package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.payload.request.person.PersonRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/people")
public class PersonController {

    @PostMapping
    public ResponseEntity<?> addNewVenue(@Valid @RequestBody PersonRequest personRequest) {

        return ResponseEntity.ok().build();

    }
}
