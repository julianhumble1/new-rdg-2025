package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Production savedProduction = productionRepository.save(production);

        return savedProduction;

    }

}
