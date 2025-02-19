package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.ProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductionService {

    private ProductionRepository productionRepository;
    private VenueService venueService;

    @Autowired
    public ProductionService(ProductionRepository productionRepository, VenueService venueService) {
        this.productionRepository = productionRepository;
        this.venueService = venueService;
    }

    // CRUD METHODS

    public Production addNewProduction(ProductionRequest productionRequest) {

        Venue venue = getVenueFromService(productionRequest);

        updateProductionRequestNameIfRepeatPerformance(productionRequest);

        Production production = new Production();
        updateProductionFromRequest(productionRequest, production, venue);

        return saveProductionToDatabase(production);

    }

    public List<Production> getAllProductions() {

        try {
            return productionRepository.findAll();
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Production getProductionById(int productionId) {
        try {
            return productionRepository.findById(productionId)
                    .orElseThrow(() -> new EntityNotFoundException("No Production with this id"));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Production updateProduction(int productionId, ProductionRequest productionRequest) {
        Production production = getProductionById(productionId);

        Venue venue = getVenueFromService(productionRequest);

        if (!(productionRequest.getName().equals(production.getName()))) {
            updateProductionRequestNameIfRepeatPerformance(productionRequest);
        }

        updateProductionFromRequest(productionRequest, production, venue);

        return saveProductionToDatabase(production);
    }

    public void deleteProductionById(int productionId) {
        Production production = getProductionById(productionId);
        venueService.removeProductionFromVenueProductionList(production);
        deleteProductionInDatabase(production);
    }

    // ADDITIONAL METHODS

    public void setProductionVenueFieldToNull(Production production) {
        production.setVenue(null);
        saveProductionToDatabase(production);
    }

    public void removePerformanceFromProductionPerformanceList(Performance performance) {
        Production production = performance.getProduction();
        List<Performance> performanceList = production.getPerformances();
        performanceList.remove(performance);
        production.setPerformances(performanceList);
        saveProductionToDatabase(production);
    }

    // PRIVATE HELPER METHODS

    private Venue getVenueFromService(ProductionRequest productionRequest) {
        Venue venue = null;
        if (productionRequest.getVenueId() > 0) {
            try {
                venue = venueService.getVenueById(productionRequest.getVenueId());
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException(ex.getMessage(), ex);
            } catch (DatabaseException ex) {
                throw new DatabaseException(ex.getMessage(), ex);
            }
        }
        return venue;
    }

    private Production saveProductionToDatabase(Production production) {
        try {
            return productionRepository.save(production);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private ProductionRequest updateProductionRequestNameIfRepeatPerformance(ProductionRequest productionRequest) {
        int timesPerformed = productionRepository.countByNameStartingWith(productionRequest.getName());

        if (timesPerformed > 0) {
            productionRequest.setName(productionRequest.getName() + " (" + (timesPerformed + 1) + ")");
        }

        return productionRequest;

    }

    private void updateProductionFromRequest(ProductionRequest productionRequest, Production production, Venue venue) {

        production.setName(productionRequest.getName());
        production.setSlug(SlugUtils.generateSlug(production.getName()));
        production.setVenue(venue);
        production.setAuthor(productionRequest.getAuthor());
        production.setDescription(productionRequest.getDescription());
        production.setAuditionDate(productionRequest.getAuditionDate());
        production.setSundowners(productionRequest.isSundowners());
        production.setNotConfirmed(productionRequest.isNotConfirmed());
        production.setFlyerFile(productionRequest.getFlyerFile());
        if (production.getCreatedAt() == null) {
            production.setCreatedAt(LocalDateTime.now());
        }
        production.setUpdatedAt(LocalDateTime.now());

    }

    private void deleteProductionInDatabase(Production production) {
        try {
            productionRepository.delete(production);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }
}
