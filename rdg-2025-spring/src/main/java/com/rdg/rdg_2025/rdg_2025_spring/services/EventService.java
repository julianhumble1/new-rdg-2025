package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Event;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.event.EventRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;
    private VenueService venueService;

    @Autowired
    public EventService(EventRepository eventRepository, VenueService venueService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
    }

    public Event addNewEvent(EventRequest newEventRequest) {
        Venue venue = getVenueFromService(newEventRequest);
        Event event = new Event();
        updateEventFromRequest(newEventRequest, event, venue);
        return saveEventToDatabase(event);
    }

    public Event getEventById(int eventId) {
        try {
            return eventRepository.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("No Event with this id"));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public List<Event> getAllEvents() {
        try {
            return eventRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }



    // PRIVATE HELPER METHODS

    private void updateEventFromRequest(EventRequest eventRequest, Event event, Venue venue) {
        event.setDescription(eventRequest.getDescription());
        event.setDateTime(eventRequest.getDateTime());
        event.setName(eventRequest.getName());
        event.setVenue(venue);
    }

    private Event saveEventToDatabase(Event event) {
        try {
            return eventRepository.save(event);
        } catch (DataIntegrityViolationException | ConstraintViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private Venue getVenueFromService(EventRequest eventRequest) {
        Venue venue = null;

        if (eventRequest.getVenueId() > 0) {
            try {
                venue = venueService.getVenueById(eventRequest.getVenueId());
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException(ex.getMessage(), ex);
            } catch (DatabaseException ex) {
                throw new DatabaseException(ex.getMessage(), ex);
            }
        }
        return venue;
    }

}
