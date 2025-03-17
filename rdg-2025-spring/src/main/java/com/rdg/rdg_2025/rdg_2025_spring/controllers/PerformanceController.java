package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.performance.PerformanceRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.performance.PerformanceResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.PerformanceService;
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
@RequestMapping("/performances")
public class PerformanceController {

    @Autowired
    PerformanceService performanceService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewPerformance(@Valid @RequestBody PerformanceRequest newPerformanceRequest) {
        try {
            Performance newPerformance = performanceService.addNewPerformance(newPerformanceRequest);
            URI location = URI.create("/performances" + newPerformance.getId());
            return ResponseEntity.created(location).body(new PerformanceResponse(newPerformance));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{performanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePerformanceById(@PathVariable int performanceId) {
        try {
            performanceService.deletePerformanceById(performanceId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Performance with this id");
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PatchMapping("/{performanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePerformance(@PathVariable int performanceId, @Valid @RequestBody PerformanceRequest performanceRequest) {
        return ResponseEntity.ok().build();
    }

}
