package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.models.Event;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.event.EventRequest;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.event.EventResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.event.EventsResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewEvent(@Valid @RequestBody EventRequest newEventRequest) {
        Event event = eventService.addNewEvent(newEventRequest);
        URI location = URI.create("/events/" + event.getId());
        return ResponseEntity.created(location).body(new EventResponse(event));
    }

    @GetMapping()
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok().body(new EventsResponse((ArrayList<Event>) events));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable int eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok().body(new EventResponse(event));
    }

    @PatchMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEvent(@PathVariable int eventId, @Valid @RequestBody EventRequest updateEventRequest) {
        Event event = eventService.updateEvent(eventId, updateEventRequest);
        return ResponseEntity.ok().body(new EventResponse(event));
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable int eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}