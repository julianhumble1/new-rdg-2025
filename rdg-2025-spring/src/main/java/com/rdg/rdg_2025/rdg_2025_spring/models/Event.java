package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="events")
@Getter @Setter @NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private LocalDateTime dateTime;

    @Column(length = 6000)
    private String description;

    @ManyToOne
    @JsonManagedReference
    private Venue venue;

    public Event(LocalDateTime dateTime, String name, String description) {
        this.dateTime = dateTime;
        this.name = name;
        this.description = description;
    }
}
