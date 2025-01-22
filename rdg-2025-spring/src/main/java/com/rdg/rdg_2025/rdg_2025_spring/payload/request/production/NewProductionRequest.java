package com.rdg.rdg_2025.rdg_2025_spring.payload.request.production;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
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

    public NewProductionRequest(String name, int venueId, String author, String description, LocalDateTime auditionDate, boolean sundowners, boolean notConfirmed, String flyerFile) {
        this.name = name;
        this.venueId = venueId;
        this.author = author;
        this.description = description;
        this.auditionDate = auditionDate;
        this.sundowners = sundowners;
        this.notConfirmed = notConfirmed;
        this.flyerFile = flyerFile;
    }
}
