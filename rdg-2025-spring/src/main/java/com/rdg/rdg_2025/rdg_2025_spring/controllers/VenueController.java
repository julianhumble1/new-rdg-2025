package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.NewVenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue.VenueResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue.VenuesResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    VenueService venueService;

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewVenue(@Valid @RequestBody NewVenueRequest newVenueRequest) {

        try {
            Venue venue = venueService.addNewVenue(newVenueRequest);
            URI location = URI.create("/venues/" + venue.getId());
            return ResponseEntity.created(location).body(new VenueResponse(venue));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllVenues() {
        try {
            List<Venue> venues = venueService.getAllVenues();
            return ResponseEntity.ok().body(new VenuesResponse((ArrayList<Venue>) venues));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


}
