package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.ProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.production.ProductionResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.production.ProductionsResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.ProductionService;
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
@RequestMapping("/productions")
public class ProductionController {

    @Autowired
    ProductionService productionService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewProduction(@Valid @RequestBody ProductionRequest productionRequest) {
        try {
            Production production = productionService.addNewProduction(productionRequest);
            URI location = URI.create("/productions/" + production.getId());
            return ResponseEntity.created(location).body(new ProductionResponse(production));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllProductions() {
        try {
            List<Production> productions = productionService.getAllProductions();
            return ResponseEntity.ok(new ProductionsResponse((ArrayList<Production>) productions));

        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{productionId}")
    public ResponseEntity<?> getProductionById(@PathVariable int productionId) {

        try {
            Production production = productionService.getProductionById(productionId);
            return ResponseEntity.ok(new ProductionResponse(production));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @PatchMapping("/{productionId}")
    public ResponseEntity<?> updateProduction(@PathVariable int productionId, @Valid @RequestBody ProductionRequest updateProductionRequest) {
        try {
            Production updateProduction = productionService.updateProduction(productionId, updateProductionRequest);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.ok("Ok");
    }


}
