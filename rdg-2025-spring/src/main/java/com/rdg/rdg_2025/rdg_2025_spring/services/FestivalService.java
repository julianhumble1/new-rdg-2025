package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals.NewFestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
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

    @Autowired
    VenueRepository venueRepository;

    public Festival addNewFestival(NewFestivalRequest newFestivalRequest) {

        Venue venue = null;

        if (newFestivalRequest.getVenueId() != 0) {
            try {
                venue = venueRepository.findById(newFestivalRequest.getVenueId())
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + newFestivalRequest.getVenueId()));
            } catch (DataAccessException ex) {
                throw new DatabaseException(ex.getMessage());
            }
        }

        Festival festival = new Festival(
                newFestivalRequest.getName(),
                venue,
                newFestivalRequest.getYear(),
                newFestivalRequest.getMonth(),
                newFestivalRequest.getDescription()
        );

        try {
            Festival savedFestival = festivalRepository.save(festival);
            return savedFestival;
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }

    }

    public List<Festival> getAllFestivals() {
        try {
            List<Festival> festivals = festivalRepository.findAll();
            return festivals;
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

}
