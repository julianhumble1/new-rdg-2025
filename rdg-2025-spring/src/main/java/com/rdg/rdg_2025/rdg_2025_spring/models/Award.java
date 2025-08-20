package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="awards")
@Getter @Setter @NoArgsConstructor
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String name;

    @ManyToOne
    @JsonManagedReference
    private Festival festival;

    @ManyToOne
    @JsonManagedReference
    private Person person;

    @ManyToOne
    @JsonManagedReference
    private Production production;

    public Award(int id, String name, Festival festival, Person person, Production production) {
        this.id = id;
        this.name = name;
        this.festival = festival;
        this.person = person;
        this.production = production;
    }
}
