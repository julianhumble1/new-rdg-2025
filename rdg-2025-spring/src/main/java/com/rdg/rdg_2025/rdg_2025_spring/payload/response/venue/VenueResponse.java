package com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;

import java.util.ArrayList;
import java.util.List;

public class VenueResponse {

    private Venue venue;

    private List<Production> productions;

    public VenueResponse(Venue venue) {
        this.venue = venue;
        this.productions = venue.getProductions();
    }

    @Override
    public String toString() {
        return "VenueResponse{" +
                "venue=" + venue +
                ", productions=" + productions +
                '}';
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }
}
