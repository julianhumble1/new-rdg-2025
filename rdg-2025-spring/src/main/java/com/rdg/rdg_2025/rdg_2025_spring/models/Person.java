package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="people")
@Getter @Setter @NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(length = 2000)
    private String summary;

    private String homePhone;
    private String mobilePhone;
    private String addressStreet;
    private String addressTown;
    private String addressPostcode;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
