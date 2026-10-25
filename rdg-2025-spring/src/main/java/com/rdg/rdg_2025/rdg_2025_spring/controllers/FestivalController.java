package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festival.FestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival.FestivalResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival.FestivalsResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.FestivalService;
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
        Festival festival = festivalService.addNewFestival(festivalRequest);
        URI location = URI.create("/festivals/" + festival.getId());
        return ResponseEntity.created(location).body(new FestivalResponse(festival));
    }


    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllFestivals() {
        List<Festival> festivals = festivalService.getAllFestivals();
        return ResponseEntity.ok(new FestivalsResponse((ArrayList<Festival>) festivals));
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<?> getFestivalById(@PathVariable int festivalId) {
        Festival festival = festivalService.getFestivalById(festivalId);
        return ResponseEntity.ok(new FestivalResponse(festival));
    }

    @DeleteMapping("/{festivalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFestivalById(@PathVariable int festivalId) {
        festivalService.deleteFestivalById(festivalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{festivalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFestival(@PathVariable int festivalId, @Valid @RequestBody FestivalRequest festivalRequest) {
        Festival festival = festivalService.updateFestival(festivalId, festivalRequest);
        return ResponseEntity.ok(new FestivalResponse(festival));
    }

}