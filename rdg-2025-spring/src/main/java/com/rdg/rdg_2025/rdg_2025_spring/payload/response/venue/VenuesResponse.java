package com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue;

import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;

import java.util.ArrayList;

public class VenuesResponse {

    private ArrayList<Venue> venues;

    public VenuesResponse(ArrayList<Venue> venues) {
        this.venues = venues;
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public void setVenues(ArrayList<Venue> venues) {
        this.venues = venues;
    }
}
