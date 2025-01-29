package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.*;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="venues")
@Getter @Setter @NoArgsConstructor @ToString(exclude = "productions")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String notes;
    private String postcode;
    private String address;
    private String town;
    private String url;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<Production> productions = new ArrayList<>();

    @OneToMany(mappedBy= "venue", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<Festival> festivals = new ArrayList<>();

    public Venue(String name, String notes, String postcode, String address, String town, String url) {
        this.name = name;
        this.notes = notes;
        this.postcode = postcode;
        this.address = address;
        this.town = town;
        this.url = url;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.slug = SlugUtils.generateSlug(name);
    }
}
