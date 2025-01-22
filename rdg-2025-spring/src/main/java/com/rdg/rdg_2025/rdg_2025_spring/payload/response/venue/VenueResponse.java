package com.rdg.rdg_2025.rdg_2025_spring.payload.response.venue;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class VenueResponse {

    private Venue venue;

    private List<Production> productions;

    private List<Festival> festivals;

    public VenueResponse(Venue venue) {
        this.venue = venue;
        this.productions = venue.getProductions();
        this.festivals = venue.getFestivals();
    }
}
