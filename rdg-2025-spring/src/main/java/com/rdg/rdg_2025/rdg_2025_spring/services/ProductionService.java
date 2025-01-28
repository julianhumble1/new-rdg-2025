package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.ProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
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

    @Autowired
    private ProductionRepository productionRepository;

    @Autowired
    private VenueRepository venueRepository;

    public Production addNewProduction(ProductionRequest productionRequest) {

        Venue venue = null;
        // if venue provided, check it exists
        if (productionRequest.getVenueId() != 0) {
            try {
            venue = venueRepository.findById(productionRequest.getVenueId())
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + productionRequest.getVenueId()));
            } catch (DataAccessException ex) {
                throw new DatabaseException(ex.getMessage());
            }
        }

        updateProductionRequestNameIfRepeatPerformance(productionRequest);

        Production production = new Production();
        updateProductionFromRequest(productionRequest, production, venue);

        try {
            return productionRepository.save(production);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }

    }

    public List<Production> getAllProductions() {

        try {
            List<Production> productions = productionRepository.findAll();
            return productions;
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Production getProductionById(int productionId) {
        try {
            Production production = productionRepository.findById(productionId)
                    .orElseThrow(() -> new EntityNotFoundException("No Production with this id"));
            return production;
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Production updateProduction(int productionId, ProductionRequest productionRequest) {
        try {
            Production production = productionRepository.findById(productionId).orElseThrow(() -> new EntityNotFoundException("No Production with this id"));

            Venue associatedVenue = null;
            if (productionRequest.getVenueId() > 0) {
                associatedVenue = venueRepository.findById(productionRequest.getVenueId()).orElseThrow(() -> new EntityNotFoundException("No Venue with this id"));
            }

            if (!(productionRequest.getName().equals(production.getName()))) {
                updateProductionRequestNameIfRepeatPerformance(productionRequest);
            }

            updateProductionFromRequest(productionRequest, production, associatedVenue);

            return productionRepository.save(production);

        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    // PRIVATE HELPER METHODS

    private ProductionRequest updateProductionRequestNameIfRepeatPerformance(ProductionRequest productionRequest) {
        int timesPerformed = productionRepository.countByNameStartingWith(productionRequest.getName());
        System.out.println(timesPerformed);

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

}
