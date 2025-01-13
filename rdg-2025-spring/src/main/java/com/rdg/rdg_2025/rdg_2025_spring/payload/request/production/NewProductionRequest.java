package com.rdg.rdg_2025.rdg_2025_spring.payload.request.production;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class NewProductionRequest {

    @NotBlank
    private String name;

    private int venueId;
    private String author;
    private String description;
    private LocalDateTime auditionDate;
    private boolean sundowners;
    private boolean notConfirmed;
    private String flyerFile;

    public NewProductionRequest() {
    }

    public NewProductionRequest(String name, int venueId, String author, String description, LocalDateTime date, boolean sundowners, boolean notConfirmed, String flyerFile) {
        this.name = name;
        this.venueId = venueId;
        this.author = author;
        this.description = description;
        this.auditionDate = date;
        this.sundowners = sundowners;
        this.notConfirmed = notConfirmed;
        this.flyerFile = flyerFile;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getAuditionDate() {
        return auditionDate;
    }

    public void setAuditionDate(LocalDateTime auditionDate) {
        this.auditionDate = auditionDate;
    }

    public boolean isSundowners() {
        return sundowners;
    }

    public void setSundowners(boolean sundowners) {
        this.sundowners = sundowners;
    }

    public boolean isNotConfirmed() {
        return notConfirmed;
    }

    public void setNotConfirmed(boolean notConfirmed) {
        this.notConfirmed = notConfirmed;
    }

    public String getFlyerFile() {
        return flyerFile;
    }

    public void setFlyerFile(String flyerFile) {
        this.flyerFile = flyerFile;
    }
}
