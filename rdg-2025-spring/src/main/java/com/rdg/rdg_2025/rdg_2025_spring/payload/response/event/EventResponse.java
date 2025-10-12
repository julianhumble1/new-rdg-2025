package com.rdg.rdg_2025.rdg_2025_spring.payload.response.event;

import com.rdg.rdg_2025.rdg_2025_spring.models.Event;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EventResponse {

    private Event event;

    public EventResponse(Event event) {
        this.event = event;
    }
}
