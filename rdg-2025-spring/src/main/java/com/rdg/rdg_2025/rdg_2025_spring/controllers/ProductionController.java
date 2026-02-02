package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.ProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.production.ProductionResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.production.ProductionsResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.ProductionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        long start = System.currentTimeMillis();
        Logger log = LoggerFactory.getLogger(getClass());

        log.info("getAllProductions - request received");

        try {
            List<Production> productions = productionService.getAllProductions();
            long duration = System.currentTimeMillis() - start;
            log.info("getAllProductions - returning {} productions in {} ms",
                    productions.size(), duration);

            return ResponseEntity.ok(new ProductionsResponse(productions));

        } catch (DatabaseException ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("getAllProductions - database error after {} ms: {}",
                    duration, ex.getMessage(), ex);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduction(@PathVariable int productionId, @Valid @RequestBody ProductionRequest updateProductionRequest) {
        try {
            Production updatedProduction = productionService.updateProduction(productionId, updateProductionRequest);
            return ResponseEntity.ok(new ProductionResponse(updatedProduction));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{productionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProductionById(@PathVariable int productionId) {
        try {
            productionService.deleteProductionById(productionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/future")
    public ResponseEntity<?> getProductionsWithFuturePerformances() {
        try {
            List<Production> productions = productionService.getProductionsWithFuturePerformances();
            return ResponseEntity.ok().body(new ProductionsResponse(productions));
        } catch (DatabaseException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
