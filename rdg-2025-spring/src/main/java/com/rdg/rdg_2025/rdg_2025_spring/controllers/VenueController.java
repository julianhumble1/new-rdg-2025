package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.NewVenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.MessageResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue.VenueResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue.VenuesResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.VenueService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    VenueService venueService;

    @PostMapping()
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

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllVenues() {
        try {
            List<Venue> venues = venueService.getAllVenues();
            return ResponseEntity.ok().body(new VenuesResponse((ArrayList<Venue>) venues));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<?> getVenueById(@PathVariable int venueId) {
        try {
            Venue venue = venueService.getVenueById(venueId);
            return ResponseEntity.ok().body(new VenueResponse(venue));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteVenueById(@PathVariable int id) {
        try {
            boolean deleted = venueService.deleteVenueById(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new MessageResponse("Could not delete as venue does not exist"));
            }
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


}
