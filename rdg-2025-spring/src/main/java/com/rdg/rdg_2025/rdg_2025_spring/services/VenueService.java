package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.VenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VenueService {

    @Autowired
    VenueRepository venueRepository;

    // CRUD METHODS

    public Venue addNewVenue(VenueRequest newVenueRequest) {

        Venue venue = new Venue();
        updateVenueFromRequest(newVenueRequest, venue);

        return saveVenueToDatabase(venue);
    }

    public List<Venue> getAllVenues() {
        try {
            return venueRepository.findAll();
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Venue getVenueById(int venueId) {
        try {
            return venueRepository.findById(venueId)
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + venueId));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Venue updateVenue(int venueId, VenueRequest updateVenueRequest) {
        Venue venue = getVenueById(venueId);

        updateVenueFromRequest(updateVenueRequest, venue);

        return saveVenueToDatabase(venue);
    }

    public boolean deleteVenueById(int venueId) {
        try {
            if (venueRepository.existsById(venueId)) {
                venueRepository.deleteById(venueId);
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    // ADDITIONAL METHODS

    public void removeProductionFromVenueProductionList(Production production) {
        if (production.getVenue() != null) {
            Venue venue = getVenueById(production.getVenue().getId());
            List<Production> productionList = venue.getProductions();
            productionList.remove(production);
            venue.setProductions(productionList);
            saveVenueToDatabase(venue);
        }
    }

    // PRIVATE HELPER METHODS

    private Venue updateVenueFromRequest(VenueRequest venueRequest, Venue venue) {
        if (venueRequest.getName() != null) {
            venue.setName(venueRequest.getName());
            venue.setSlug(SlugUtils.generateSlug(venue.getName()));
        }
        venue.setNotes(venueRequest.getNotes());
        venue.setPostcode(venueRequest.getPostcode());
        venue.setAddress(venueRequest.getAddress());
        venue.setTown(venueRequest.getTown());
        venue.setUrl(venueRequest.getUrl());
        if (venue.getCreatedAt() == null) {
            venue.setCreatedAt(LocalDateTime.now());
        }
        venue.setUpdatedAt(LocalDateTime.now());

        return venue;
    }

    private Venue saveVenueToDatabase(Venue venue) {
        try {
            return venueRepository.save(venue);
        } catch (DataIntegrityViolationException | ConstraintViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

}
