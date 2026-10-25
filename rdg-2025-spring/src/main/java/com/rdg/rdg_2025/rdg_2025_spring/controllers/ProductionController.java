package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.ProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.production.ProductionResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.production.ProductionsResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.ProductionService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/productions")
public class ProductionController {

    @Autowired
    ProductionService productionService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewProduction(@Valid @RequestBody ProductionRequest productionRequest) {
        Production production = productionService.addNewProduction(productionRequest);
        URI location = URI.create("/productions/" + production.getId());
        return ResponseEntity.created(location).body(new ProductionResponse(production));
    }

    @GetMapping()
    public ResponseEntity<?> getAllProductions() {
        long start = System.currentTimeMillis();
        Logger log = LoggerFactory.getLogger(getClass());

        log.info("getAllProductions - request received");
        List<Production> productions = productionService.getAllProductions();
        long duration = System.currentTimeMillis() - start;
        log.info("getAllProductions - returning {} productions in {} ms",
                productions.size(), duration);

        return ResponseEntity.ok(new ProductionsResponse(productions));
    }

    @GetMapping("/{productionId}")
    public ResponseEntity<?> getProductionById(@PathVariable int productionId) {
        Production production = productionService.getProductionById(productionId);
        return ResponseEntity.ok(new ProductionResponse(production));
    }

    @PatchMapping("/{productionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduction(@PathVariable int productionId, @Valid @RequestBody ProductionRequest updateProductionRequest) {
        Production updatedProduction = productionService.updateProduction(productionId, updateProductionRequest);
        return ResponseEntity.ok(new ProductionResponse(updatedProduction));
    }

    @DeleteMapping("/{productionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProductionById(@PathVariable int productionId) {
        productionService.deleteProductionById(productionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/future")
    public ResponseEntity<?> getProductionsWithFuturePerformances() {
        List<Production> productions = productionService.getProductionsWithFuturePerformances();
        List<ProductionResponse> productionResponses = productions.stream().map(ProductionResponse::new).toList();
        return ResponseEntity.ok().body(Map.of("productions", productionResponses));
    }

}
