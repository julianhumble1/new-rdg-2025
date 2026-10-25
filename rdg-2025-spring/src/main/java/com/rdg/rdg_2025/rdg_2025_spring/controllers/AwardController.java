package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.award.AwardRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.award.AwardResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.AwardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        Award award = awardService.addNewAward(awardRequest);
        URI location = URI.create("/awards/" + award.getId());
        return ResponseEntity.created(location).body(new AwardResponse(award));
    }

    @GetMapping("/{awardId}")
    public ResponseEntity<?> getAwardById(@PathVariable int awardId) {
        Award award = awardService.getAwardById(awardId);
        return ResponseEntity.ok(new AwardResponse(award));
    }

    @PatchMapping("/{awardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAward(@PathVariable int awardId, @Valid @RequestBody AwardRequest awardRequest) {
        Award award = awardService.updateAward(awardId, awardRequest);
        return ResponseEntity.ok(new AwardResponse(award));
    }

    @DeleteMapping("/{awardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAwardById(@PathVariable int awardId) {
        awardService.deleteAwardById(awardId);
        return ResponseEntity.noContent().build();
    }

}
