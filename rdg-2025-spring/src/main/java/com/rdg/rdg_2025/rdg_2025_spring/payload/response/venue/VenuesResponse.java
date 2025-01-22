package com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue;

import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class VenuesResponse {

    private ArrayList<Venue> venues;

    public VenuesResponse(ArrayList<Venue> venues) {
        this.venues = venues;
    }
}
