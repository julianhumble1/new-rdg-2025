package com.rdg.rdg_2025.rdg_2025_spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="production_images")
@Getter @Setter @NoArgsConstructor
public class ProductionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String cloudinaryRef;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Production production;

    @Column(length = 2000)
    private String description;
}
