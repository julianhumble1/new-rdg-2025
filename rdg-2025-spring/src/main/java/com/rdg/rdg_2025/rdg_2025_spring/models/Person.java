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

    private String home_phone;

    private String mobile_phone;

    private String address_street;

    private String address_town;

    private String address_postcode;

    @Column(nullable = false, unique = true)
    private String slug;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Person(String firstName, String lastName, String summary, String home_phone, String mobile_phone, String address_street, String address_town, String address_postcode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        this.home_phone = home_phone;
        this.mobile_phone = mobile_phone;
        this.address_street = address_street;
        this.address_town = address_town;
        this.address_postcode = address_postcode;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.slug = SlugUtils.generateSlug(firstName, lastName);
    }
}
