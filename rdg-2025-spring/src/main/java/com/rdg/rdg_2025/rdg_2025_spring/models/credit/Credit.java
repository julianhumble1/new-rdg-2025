package com.rdg.rdg_2025.rdg_2025_spring.models.credit;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="credits")
@Getter @Setter @NoArgsConstructor @ToString
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditType type;

    @ManyToOne
    @JsonManagedReference
    private Person person;

    @ManyToOne
    @JsonManagedReference
    private Production production;

    private String summary;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Credit(String name, CreditType type, Person person, Production production, String summary) {
        this.name = name;
        this.type = type;
        this.person = person;
        this.production = production;
        this.summary = summary;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
