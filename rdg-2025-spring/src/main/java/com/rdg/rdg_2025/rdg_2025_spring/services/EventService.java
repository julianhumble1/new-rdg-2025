package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Event;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.event.EventRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.EventRepository;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event addNewEvent(EventRequest newEventRequest) {
        Event event = new Event();

        updateEventFromRequest(newEventRequest, event);

        return saveEventToDatabase(event);
    }

    private void updateEventFromRequest(EventRequest eventRequest, Event event) {
        event.setDescription(eventRequest.getDescription());
        event.setDateTime(eventRequest.getDateTime());
        event.setName(eventRequest.getName());
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

}
