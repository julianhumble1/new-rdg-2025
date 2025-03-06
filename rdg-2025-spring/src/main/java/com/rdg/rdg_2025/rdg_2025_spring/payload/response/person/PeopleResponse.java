package com.rdg.rdg_2025.rdg_2025_spring.payload.response.person;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PeopleResponse {

    private List<Person> people;

    public PeopleResponse(List<Person> people) {
        this.people = people;
    }
}
