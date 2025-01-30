package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.performance.PerformanceRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;

    private ProductionService productionService;
    private VenueService venueService;
    private FestivalService festivalService;

    public PerformanceService(ProductionService productionService, VenueService venueService, FestivalService festivalService) {
        this.productionService = productionService;
        this.venueService = venueService;
        this.festivalService = festivalService;
    }

    // METHODS

    public Performance addNewPerformance(PerformanceRequest newPerformanceRequest) {
        Venue venue = retrieveVenueFromService(newPerformanceRequest);

        Production production = retrieveProductionFromService(newPerformanceRequest);

        return new Performance();
    }

    // PRIVATE HELPER METHODS

    private Venue retrieveVenueFromService(PerformanceRequest performanceRequest) {
        Venue venue = null;
        try {
            venue = venueService.getVenueById(performanceRequest.getVenueId());
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ex.getMessage(), ex);
        } catch (DatabaseException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }

        return venue;
    }

    private Production retrieveProductionFromService(PerformanceRequest performanceRequest) {
        Production production = null;
        try {
            production = productionService.getProductionById(performanceRequest.getProductionId());
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ex.getMessage(), ex);
        } catch (DatabaseException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }

        return production;
    }


}
