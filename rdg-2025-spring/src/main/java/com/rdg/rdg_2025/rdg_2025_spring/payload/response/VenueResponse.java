package com.rdg.rdg_2025.rdg_2025_spring.payload.response;

import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;

public class VenueResponse {

    private Venue venue;

    public VenueResponse(Venue venue) {
        this.venue = venue;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
