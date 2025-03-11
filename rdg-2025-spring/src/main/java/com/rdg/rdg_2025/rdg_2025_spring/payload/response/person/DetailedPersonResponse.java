package com.rdg.rdg_2025.rdg_2025_spring.payload.response.person;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetailedPersonResponse {

    private Person person;

    private PersonResponseType responseType = PersonResponseType.DETAILED;

    public DetailedPersonResponse(Person person) {
        this.person = person;
    }
}
