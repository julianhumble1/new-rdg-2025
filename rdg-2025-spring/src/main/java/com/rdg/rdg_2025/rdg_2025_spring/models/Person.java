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

    public Person(String firstName, String lastName, String summary, String home_phone, String mobile_phone, String address_street, String address_town, String address_postcode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        this.homePhone = home_phone;
        this.mobilePhone = mobile_phone;
        this.addressStreet = address_street;
        this.addressTown = address_town;
        this.addressPostcode = address_postcode;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.slug = SlugUtils.generateSlug(firstName, lastName);
    }
}
