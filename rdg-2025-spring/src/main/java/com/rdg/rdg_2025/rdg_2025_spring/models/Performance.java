package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "performances", indexes = {
        @Index(name = "idx_perf_time",          columnList = "time"),
        @Index(name = "idx_perf_production_id", columnList = "production_id"),
        @Index(name = "idx_perf_venue_id",      columnList = "venue_id"),
        @Index(name = "idx_perf_festival_id",   columnList = "festival_id")
})
@Getter @Setter @NoArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonManagedReference
    private Production production;

    @ManyToOne
    @JsonManagedReference
    private Venue venue;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Festival festival;

    private String description;
    private BigDecimal standardPrice;
    private BigDecimal concessionPrice;
    private String boxOffice;
    private LocalDateTime time;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Performance(Production production, Venue venue, Festival festival, LocalDateTime time, String description, BigDecimal standardPrice, BigDecimal concessionPrice, String boxOffice) {
        this.production = production;
        this.venue = venue;
        this.festival = festival;
        this.time = time;
        this.description = description;
        this.standardPrice = standardPrice;
        this.concessionPrice = concessionPrice;
        this.boxOffice = boxOffice;
    }
}
