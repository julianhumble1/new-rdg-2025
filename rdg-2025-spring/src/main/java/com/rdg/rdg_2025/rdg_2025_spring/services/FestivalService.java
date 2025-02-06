package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festival.NewFestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalService {

    @Autowired
    FestivalRepository festivalRepository;

    public FestivalService(VenueService venueService) {
        this.venueService = venueService;
    }

    private VenueService venueService;

    // CRUD METHODS

    public Festival addNewFestival(NewFestivalRequest newFestivalRequest) {

        Venue venue = getVenueFromService(newFestivalRequest);

        Festival festival = new Festival(
                newFestivalRequest.getName(),
                venue,
                newFestivalRequest.getYear(),
                newFestivalRequest.getMonth(),
                newFestivalRequest.getDescription()
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
            festivalRepository.delete(festival);
            return true;
        } catch (EntityNotFoundException ex) {
            return false;
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    // ADDITIONAL METHODS

    public void setFestivalVenueFieldToNull(Festival festival) {
        festival.setVenue(null);
        saveFestivalToDatabase(festival);
    }

    // PRIVATE HELPER METHODS

    private Venue getVenueFromService(NewFestivalRequest newFestivalRequest) {
        Venue venue = null;
        if (newFestivalRequest.getVenueId() > 0) {
            try {
                venue = venueService.getVenueById(newFestivalRequest.getVenueId());
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
