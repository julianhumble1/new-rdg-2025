package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festival.FestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival.FestivalResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival.FestivalsResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.FestivalService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/festivals")
public class FestivalController {

    @Autowired
    FestivalService festivalService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewFestival(@Valid @RequestBody FestivalRequest festivalRequest) {
        try {
            Festival festival = festivalService.addNewFestival(festivalRequest);
            URI location = URI.create("/festivals/" + festival.getId());
            return ResponseEntity.created(location).body(new FestivalResponse(festival));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllFestivals() {
        try {
            List<Festival> festivals = festivalService.getAllFestivals();
            return ResponseEntity.ok(new FestivalsResponse((ArrayList<Festival>) festivals));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<?> getFestivalById(@PathVariable int festivalId) {
        try {
            Festival festival = festivalService.getFestivalById(festivalId);
            return ResponseEntity.ok(new FestivalResponse(festival));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{festivalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFestivalById(@PathVariable int festivalId) {
        try {
            if (festivalService.deleteFestivalById(festivalId)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No festival with id: " + festivalId);
            }
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PatchMapping("/{festivalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFestival(@PathVariable int festivalId, @Valid @RequestBody FestivalRequest festivalRequest) {
        return ResponseEntity.ok().build();
    }

}
