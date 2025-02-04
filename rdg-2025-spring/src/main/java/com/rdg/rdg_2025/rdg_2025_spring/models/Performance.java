package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="performances")
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

    @ManyToOne
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
