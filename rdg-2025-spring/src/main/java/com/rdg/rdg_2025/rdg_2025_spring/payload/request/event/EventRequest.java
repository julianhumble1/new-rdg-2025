package com.rdg.rdg_2025.rdg_2025_spring.payload.request.event;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class EventRequest {

    @NotBlank
    private String name;

    @NotNull
    LocalDateTime dateTime;

    private String description;

    public EventRequest(String name, LocalDateTime dateTime, String description) {
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
    }
}
