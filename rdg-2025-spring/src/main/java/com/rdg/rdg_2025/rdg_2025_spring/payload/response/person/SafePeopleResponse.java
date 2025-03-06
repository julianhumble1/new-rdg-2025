package com.rdg.rdg_2025.rdg_2025_spring.payload.response.person;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SafePeopleResponse {

    private List<Person> people;

    public SafePeopleResponse(List<Person> people) {
        people.forEach((Person person) -> {
            person.setHomePhone(null);
            person.setMobilePhone(null);
            person.setAddressStreet(null);
            person.setAddressTown(null);
            person.setAddressPostcode(null);
        });
        this.people = people;
    }
}
