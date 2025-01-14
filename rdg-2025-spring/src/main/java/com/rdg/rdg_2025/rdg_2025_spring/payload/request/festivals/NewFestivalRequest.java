package com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewFestivalRequest {

    @NotBlank
    private String name;

    private int venueId;

    @NotNull
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

    public NewFestivalRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
