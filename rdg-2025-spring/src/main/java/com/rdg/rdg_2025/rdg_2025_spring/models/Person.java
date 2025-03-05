package com.rdg.rdg_2025.rdg_2025_spring.models;

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

}
