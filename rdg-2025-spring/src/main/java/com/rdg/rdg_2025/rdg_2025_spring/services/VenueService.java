package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.NewVenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class VenueService {

    @Autowired
    VenueRepository venueRepository;

    public Venue addNewVenue(NewVenueRequest newVenueRequest) {


        Venue venue = new Venue(
                newVenueRequest.getName(),
                newVenueRequest.getNotes(),
                newVenueRequest.getPostcode(),
                newVenueRequest.getAddress(),
                newVenueRequest.getTown(),
                newVenueRequest.getUrl()
        );

        try {
            venueRepository.save(venue);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }

        return venue;

    }
}
