package com.rdg.rdg_2025.rdg_2025_spring.payload.response.event;

import com.rdg.rdg_2025.rdg_2025_spring.models.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class EventsResponse {
    private ArrayList<Event> events;

    public EventsResponse(ArrayList<Event> events) {
        this.events = events;
    }
}
