package com.rdg.rdg_2025.rdg_2025_spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Production production;

    @ManyToOne
    private Venue venue;

    @ManyToOne
    private Festival festival;

    private String description;
    private BigDecimal standardPrice;
    private BigDecimal concessionPrice;
    private String boxOffice;
    private LocalDateTime time;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
