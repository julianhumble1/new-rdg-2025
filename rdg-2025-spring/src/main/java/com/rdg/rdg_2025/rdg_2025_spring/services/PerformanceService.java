package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.performance.PerformanceRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        Festival festival = retrieveFestivalFromService(newPerformanceRequest);

        Performance performance = new Performance();
        updatePerformanceFromRequest(newPerformanceRequest, venue, production, festival, performance);

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

    private Festival retrieveFestivalFromService(PerformanceRequest performanceRequest) {
        Festival festival = null;
        if (performanceRequest.getFestivalId() > 0) {
            try {
                festival = festivalService.getFestivalById(performanceRequest.getFestivalId());
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException(ex.getMessage(), ex);
            } catch (DatabaseException ex) {
                throw new DatabaseException(ex.getMessage(), ex);
            }
        }
        return festival;
    }

    private Performance updatePerformanceFromRequest(PerformanceRequest performanceRequest,  Venue venue, Production production, Festival festival, Performance performance) {
        performance.setVenue(venue);
        performance.setProduction(production);
        performance.setFestival(festival);
        performance.setDescription(performanceRequest.getDescription());
        performance.setStandardPrice(performanceRequest.getStandardPrice());
        performance.setConcessionPrice(performanceRequest.getConcessionPrice());
        performance.setBoxOffice(performanceRequest.getBoxOffice());
        performance.setTime(performanceRequest.getTime());
        if (performance.getCreatedAt() == null) {
            performance.setCreatedAt(LocalDateTime.now());
        }
        performance.setUpdatedAt(LocalDateTime.now());
        return performance;
    }


}
