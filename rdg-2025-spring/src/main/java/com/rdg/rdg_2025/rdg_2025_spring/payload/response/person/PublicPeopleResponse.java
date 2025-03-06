package com.rdg.rdg_2025.rdg_2025_spring.payload.response.person;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PublicPeopleResponse {

    private List<Person> people;

    private PersonResponseType responseType = PersonResponseType.PUBLIC;

    public PublicPeopleResponse(List<Person> people) {
        people.forEach((Person person) -> {
            person.setHomePhone("");
            person.setMobilePhone("");
            person.setAddressStreet("");
            person.setAddressTown("");
            person.setAddressPostcode("");
        });
        this.people = people;
    }
}
