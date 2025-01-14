package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
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

    public Production addNewProduction(NewProductionRequest newProductionRequest) {

        Venue venue = null;

        // if venue provided, check it exists
        if (newProductionRequest.getVenueId() != 0) {
            venue = venueRepository.findById(newProductionRequest.getVenueId())
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + newProductionRequest.getVenueId()));
        }

        Production production = new Production(
                newProductionRequest.getName(),
                venue,
                newProductionRequest.getAuthor(),
                newProductionRequest.getDescription(),
                newProductionRequest.getAuditionDate(),
                newProductionRequest.isSundowners(),
                newProductionRequest.isNotConfirmed(),
                newProductionRequest.getFlyerFile()
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

        List<Production> productions = productionRepository.findAll();

        return productions;
    }

}
