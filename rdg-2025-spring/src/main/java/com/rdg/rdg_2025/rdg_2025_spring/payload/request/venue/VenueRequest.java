package com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VenueRequest {

    @NotBlank
    private String name;

    private String notes;
    private String postcode;
    private String address;
    private String town;
    private String url;

    public VenueRequest(String name, String notes, String postcode, String address, String town, String url) {
        this.name = name;
        this.notes = notes;
        this.postcode = postcode;
        this.address = address;
        this.town = town;
        this.url = url;
    }
}
