package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals.NewFestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival.NewFestivalResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.FestivalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/festivals")
public class FestivalController {

    @Autowired
    FestivalService festivalService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewFestival(@Valid @RequestBody NewFestivalRequest newFestivalRequest) {
        try {
            Festival festival = festivalService.addNewFestival(newFestivalRequest);
            URI location = URI.create("/festivals/" + festival.getId());
            return ResponseEntity.created(location).body(new NewFestivalResponse(festival));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
