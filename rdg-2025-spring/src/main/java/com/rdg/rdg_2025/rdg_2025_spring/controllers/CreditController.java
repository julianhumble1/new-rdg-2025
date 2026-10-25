package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.credit.CreditRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.credit.CreditResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.CreditService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        Credit credit = creditService.addNewCredit(creditRequest);
        URI location = URI.create("/credits/" + credit.getId());
        return ResponseEntity.created(location).body(new CreditResponse(credit));
    }

    @GetMapping("/{creditId}")
    public ResponseEntity<?> getCreditById(@PathVariable int creditId) {
        Credit credit = creditService.getCreditById(creditId);
        return ResponseEntity.ok(new CreditResponse(credit));
    }

    @PatchMapping("/{creditId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCredit(@PathVariable int creditId, @Valid @RequestBody CreditRequest creditRequest) {
        Credit credit = creditService.updateCredit(creditId, creditRequest);
        return ResponseEntity.ok(new CreditResponse(credit));
    }

    @DeleteMapping("/{creditId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCreditById(@PathVariable int creditId) {
        creditService.deleteCreditById(creditId);
        return ResponseEntity.noContent().build();
    }
}