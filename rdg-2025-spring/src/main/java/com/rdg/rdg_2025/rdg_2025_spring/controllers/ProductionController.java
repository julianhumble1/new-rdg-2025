package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.production.ProductionResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.ProductionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/productions")
public class ProductionController {

    @Autowired
    ProductionService productionService;

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewProduction(@Valid @RequestBody NewProductionRequest newProductionRequest) {
        try {
            Production production = productionService.addNewProduction(newProductionRequest);
            URI location = URI.create("/productions/" + production.getId());
            return ResponseEntity.created(location).body(new ProductionResponse(production));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
