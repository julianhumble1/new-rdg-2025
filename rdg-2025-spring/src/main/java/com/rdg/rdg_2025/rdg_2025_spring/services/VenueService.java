package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue.VenueRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
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

    public Venue addNewVenue(VenueRequest newVenueRequest) {

        Venue venue = new Venue(
                newVenueRequest.getName(),
                newVenueRequest.getNotes(),
                newVenueRequest.getPostcode(),
                newVenueRequest.getAddress(),
                newVenueRequest.getTown(),
                newVenueRequest.getUrl()
        );

        try {
            Venue savedVenue = venueRepository.save(venue);
            return savedVenue;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public List<Venue> getAllVenues() {
        try {
            List<Venue> venues = venueRepository.findAll();
            return venues;
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Venue getVenueById(int venueId) {
        try {
            Venue venue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + venueId));
            return venue;
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public Venue updateVenue(int venueId, VenueRequest updateVenueRequest) {
        try {
            Venue venue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + venueId));

            Venue updatedVenueObject = updateVenueObject(updateVenueRequest, venue);

            Venue updatedVenue = venueRepository.save(updatedVenueObject);

            return updatedVenue;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private Venue updateVenueObject(VenueRequest updateVenueRequest, Venue venue) {
        if (updateVenueRequest.getName() != null) {
            venue.setName(updateVenueRequest.getName());
            venue.setSlug(SlugUtils.generateSlug(venue.getName()));
        }
        venue.setNotes(updateVenueRequest.getNotes());
        venue.setPostcode(updateVenueRequest.getPostcode());
        venue.setAddress(updateVenueRequest.getAddress());
        venue.setTown(updateVenueRequest.getTown());
        venue.setUrl(updateVenueRequest.getUrl());
        venue.setUpdatedAt(LocalDateTime.now());

        return venue;
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
}
