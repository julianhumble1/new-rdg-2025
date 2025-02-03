package com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @NoArgsConstructor @ToString
public class NewFestivalRequest {

    @NotBlank
    private String name;

    private int venueId;

    @Min(1)
    private int year;

    @Max(12)
    @Min(0)
    private int month;

    private String description;

    public NewFestivalRequest(String name, int venueId, int year, int month, String description) {
        this.name = name;
        this.venueId = venueId;
        this.year = year;
        this.month = month;
        this.description = description;
    }
}
