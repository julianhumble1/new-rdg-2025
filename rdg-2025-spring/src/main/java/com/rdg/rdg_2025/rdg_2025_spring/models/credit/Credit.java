package com.rdg.rdg_2025.rdg_2025_spring.models.credit;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="credits")
@Getter @Setter @NoArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CreditType type;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Production production;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Credit(String name, CreditType type, Person person, Production production) {
        this.name = name;
        this.type = type;
        this.person = person;
        this.production = production;
    }
}
