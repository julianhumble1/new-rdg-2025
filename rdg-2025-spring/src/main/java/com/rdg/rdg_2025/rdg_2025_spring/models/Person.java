package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="people")
@Getter @Setter @NoArgsConstructor @ToString(exclude = "credits")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(length = 6000)
    private String summary;

    private String homePhone;
    private String mobilePhone;
    private String addressStreet;
    private String addressTown;
    private String addressPostcode;

    private String imageId;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "person")
    @JsonBackReference
    private List<Credit> credits = new ArrayList<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<Award> awards = new ArrayList<>();

    public Person(String firstName, String lastName, String summary, String homePhone, String mobilePhone, String addressStreet, String addressTown, String addressPostcode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        this.homePhone = homePhone;
        this.mobilePhone = mobilePhone;
        this.addressStreet = addressStreet;
        this.addressTown = addressTown;
        this.addressPostcode = addressPostcode;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.slug = SlugUtils.generateSlug(firstName, lastName);
    }
}
