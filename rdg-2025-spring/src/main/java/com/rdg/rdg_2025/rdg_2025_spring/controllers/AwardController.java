package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.award.AwardRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.award.AwardResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.AwardService;
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
@RequestMapping("/awards")
public class AwardController {

    @Autowired
    AwardService awardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewAward(@Valid @RequestBody AwardRequest awardRequest) {
        try {
            Award award = awardService.addNewAward(awardRequest);
            URI location = URI.create("/awards/" + award.getId());
            return ResponseEntity.created(location).body(new AwardResponse(award));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{awardId}")
    public ResponseEntity<?> getAwardById(@PathVariable int awardId) {
        try {
            Award award = awardService.getAwardById(awardId);
            return ResponseEntity.ok(new AwardResponse(award));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PatchMapping("/{awardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAward(@PathVariable int awardId, @Valid @RequestBody AwardRequest awardRequest) {
        try {
            Award award = awardService.updateAward(awardId, awardRequest);
            return ResponseEntity.ok(new AwardResponse(award));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{awardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAwardById(@PathVariable int awardId) {
        try {
            awardService.deleteAwardById(awardId);
            return ResponseEntity.noContent().build();
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}
