package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
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

        Production production = new Production(
                productionRequest.getName(),
                venue,
                productionRequest.getAuthor(),
                productionRequest.getDescription(),
                productionRequest.getAuditionDate(),
                productionRequest.isSundowners(),
                productionRequest.isNotConfirmed(),
                productionRequest.getFlyerFile()
        );

        Production updatedProduction = updateNameAndSlugIfRepeatPerformance(production);
        try {
            Production savedProduction = productionRepository.save(updatedProduction);

            return savedProduction;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }

    }

    private Production updateNameAndSlugIfRepeatPerformance(Production production) {
        // check if production name has already been used
        int timesPerformed = productionRepository.countByNameStartingWith(production.getName());

        if (timesPerformed > 0) {
            production.setName(production.getName() + " (" + (timesPerformed + 1) + ")");
            production.setSlug(production.getSlug() + "-" + (timesPerformed + 1));
        }

        return production;
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

    public Production updateProductionObject(int productionId, ProductionRequest productionRequest) {
        try {
            Production existingProduction = productionRepository.findById(productionId).orElseThrow(() -> new EntityNotFoundException("No Production with this id"));
            Venue venue = venueRepository.findById(productionRequest.getVenueId()).orElseThrow(() -> new EntityNotFoundException("No Venue with this id"));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        return new Production();
    }

}
