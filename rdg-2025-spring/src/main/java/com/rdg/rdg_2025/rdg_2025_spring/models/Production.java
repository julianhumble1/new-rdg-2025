package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name="productions")
@Getter @Setter @NoArgsConstructor @ToString
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Venue venue;

    private String author;
    private String description;
    private LocalDateTime auditionDate;
    private boolean sundowners;
    private boolean notConfirmed;
    private String flyerFile;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String slug;

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
