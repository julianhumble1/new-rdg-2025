package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.credit.CreditRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.credit.CreditResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.CreditService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/credits")
public class CreditController {

    @Autowired
    CreditService creditService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewCredit(@Valid @RequestBody CreditRequest creditRequest) {

        try {
            Credit credit = creditService.addNewCredit(creditRequest);
            URI location = URI.create("/credits/" + credit.getId());
            return ResponseEntity.created(location).body(new CreditResponse(credit));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

    }

    @GetMapping("/{creditId}")
    public ResponseEntity<?> getCreditById(@PathVariable int creditId) {
        try {
            Credit credit = creditService.getCreditById(creditId);
            return ResponseEntity.ok(new CreditResponse(credit));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PatchMapping("/{creditId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCredit(@PathVariable int creditId, @Valid @RequestBody CreditRequest creditRequest) {

        try {
            Credit credit = creditService.updateCredit(creditId, creditRequest);
            return ResponseEntity.ok(new CreditResponse(credit));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }


    }
}
