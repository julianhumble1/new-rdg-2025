package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.payload.request.NewVenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    VenueService venueService;

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public void addNewVenue(@Valid @RequestBody NewVenueRequest newVenueRequest) {
        System.out.println("received a request");
        System.out.println(newVenueRequest);
        venueService.addNewVenue(newVenueRequest);
    }

}
