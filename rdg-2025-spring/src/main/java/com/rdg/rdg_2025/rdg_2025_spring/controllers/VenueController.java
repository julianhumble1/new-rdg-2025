package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.VenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue.VenueResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue.VenuesResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> addNewVenue(@Valid @RequestBody VenueRequest newVenueRequest) {
        Venue venue = venueService.addNewVenue(newVenueRequest);
        URI location = URI.create("/venues/" + venue.getId());
        return ResponseEntity.created(location).body(new VenueResponse(venue));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllVenues() {
        List<Venue> venues = venueService.getAllVenues();
        return ResponseEntity.ok().body(new VenuesResponse((ArrayList<Venue>) venues));
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<?> getVenueById(@PathVariable int venueId) {
        Venue venue = venueService.getVenueById(venueId);
        return ResponseEntity.ok().body(new VenueResponse(venue));
    }


    @PatchMapping("/{venueId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateVenue(@PathVariable int venueId, @Valid @RequestBody VenueRequest updateVenueRequest) {
        Venue updatedVenue = venueService.updateVenue(venueId, updateVenueRequest);
        return ResponseEntity.ok().body(new VenueResponse(updatedVenue));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteVenueById(@PathVariable int id) {
        venueService.deleteVenueById(id);
        return ResponseEntity.noContent().build();
    }
}