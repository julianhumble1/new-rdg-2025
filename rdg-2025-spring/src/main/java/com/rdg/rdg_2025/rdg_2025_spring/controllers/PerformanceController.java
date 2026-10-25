package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.performance.PerformanceRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.performance.PerformanceResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.PerformanceService;
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
        Performance newPerformance = performanceService.addNewPerformance(newPerformanceRequest);
        URI location = URI.create("/performances" + newPerformance.getId());
        return ResponseEntity.created(location).body(new PerformanceResponse(newPerformance));
    }

    @DeleteMapping("/{performanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePerformanceById(@PathVariable int performanceId) {
        performanceService.deletePerformanceById(performanceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{performanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePerformance(
            @PathVariable int performanceId, @Valid @RequestBody PerformanceRequest performanceRequest) {
        Performance performance = performanceService.updatePerformance(performanceId, performanceRequest);
        return ResponseEntity.ok().body(new PerformanceResponse(performance));
    }

    @GetMapping("/{performanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPerformanceById(@PathVariable int performanceId) {
        Performance performance = performanceService.getPerformanceById(performanceId);
        return ResponseEntity.ok(new PerformanceResponse(performance));
    }

}