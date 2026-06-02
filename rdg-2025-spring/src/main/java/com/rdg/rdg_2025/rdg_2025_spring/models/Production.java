package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "Production.withAssociations",
        attributeNodes = {
                @NamedAttributeNode("venue")
        }
)
@Entity
@Table(name = "productions", indexes = {
        @Index(name = "idx_prod_name",     columnList = "name"),
        @Index(name = "idx_prod_venue_id", columnList = "venue_id")
})
@Getter @Setter @NoArgsConstructor @ToString(exclude = "credits")
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JsonManagedReference
    private Venue venue;

    private String author;

    @Column(length = 5000)
    private String description;
    private LocalDateTime auditionDate;

    @Column(columnDefinition = "TINYINT", length = 1)
    @Convert(converter = NumericBooleanConverter.class)
    private boolean sundowners;

    @Column(columnDefinition = "TINYINT", length = 1)
    @Convert(converter = NumericBooleanConverter.class)
    private boolean notConfirmed;

    private String flyerFile;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String slug;

    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL)
    @JsonBackReference
    @BatchSize(size = 50)
    private List<Performance> performances = new ArrayList<>();

    @OneToMany(mappedBy = "production", cascade = CascadeType.REMOVE)
    @JsonBackReference
    @BatchSize(size = 50)
    private List<Credit> credits = new ArrayList<>();

    @OneToMany(mappedBy = "production")
    @JsonBackReference
    private List<ProductionImage> productionImages = new ArrayList<>();

    @OneToMany(mappedBy = "production", cascade = CascadeType.PERSIST)
    @JsonBackReference
    @BatchSize(size = 50)
    private List<Award> awards = new ArrayList<>();

    public Production(String name, Venue venue, String author, String description, LocalDateTime auditionDate, boolean sundowners, boolean notConfirmed, String flyerFile) {
        this.name = name;
        this.venue = venue;
        this.author = author;
        this.description = description;
        this.auditionDate = auditionDate;
        this.sundowners = sundowners;
        this.notConfirmed = notConfirmed;
        this.flyerFile = flyerFile;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.slug = SlugUtils.generateSlug(name);
    }
}
