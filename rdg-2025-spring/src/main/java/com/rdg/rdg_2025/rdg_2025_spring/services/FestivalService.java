package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festival.FestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalService {

    FestivalRepository festivalRepository;

    private VenueService venueService;
    private PerformanceService performanceService;

    @Autowired
    public FestivalService(FestivalRepository festivalRepository, @Lazy VenueService venueService,@Lazy PerformanceService performanceService) {

        this.festivalRepository = festivalRepository;
        this.venueService = venueService;
        this.performanceService = performanceService;
    }

    // CRUD METHODS

    public Festival addNewFestival(FestivalRequest festivalRequest) {

        Venue venue = getVenueFromService(festivalRequest);

        Festival festival = new Festival(
                festivalRequest.getName(),
                venue,
                festivalRequest.getYear(),
                festivalRequest.getMonth(),
                festivalRequest.getDescription()
        );

        return saveFestivalToDatabase(festival);

    }

    public List<Festival> getAllFestivals() {
        try {
            return festivalRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Festival getFestivalById(int festivalId) {
        try {
            return festivalRepository.findById(festivalId)
                    .orElseThrow(() -> new EntityNotFoundException("No festival with this id: " + festivalId));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    public boolean deleteFestivalById(int festivalId) {

        try {
            Festival festival = getFestivalById(festivalId);
            venueService.removeFestivalFromVenueFestivalList(festival);
            List<Performance> performances = festival.getPerformances();
            performances.forEach((performance) -> {
                performanceService.setPerformanceFestivalFieldToNull(performance);
            });
            festivalRepository.delete(festival);
            return true;
        } catch (EntityNotFoundException ex) {
            return false;
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    public Festival updateFestival(int festivalId, FestivalRequest festivalRequest) {

        Festival festival = getFestivalById(festivalId);
        return new Festival();
    }

    // ADDITIONAL METHODS

    public void setFestivalVenueFieldToNull(Festival festival) {
        festival.setVenue(null);
        saveFestivalToDatabase(festival);
    }

    // PRIVATE HELPER METHODS

    private Venue getVenueFromService(FestivalRequest festivalRequest) {
        Venue venue = null;
        if (festivalRequest.getVenueId() > 0) {
            try {
                venue = venueService.getVenueById(festivalRequest.getVenueId());
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException(ex.getMessage(), ex);
            } catch (DatabaseException ex) {
                throw new DatabaseException(ex.getMessage(), ex);
            }
        }
        return venue;
    }

    private Festival saveFestivalToDatabase(Festival festival) {
        try {
            return festivalRepository.save(festival);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

}
