package com.rdg.rdg_2025.rdg_2025_spring.payload.response.person;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PublicPersonResponse {

    private Person person;

    private PersonResponseType responseType = PersonResponseType.PUBLIC;

    public PublicPersonResponse(Person person) {
        person.setHomePhone("");
        person.setMobilePhone("");
        person.setAddressStreet("");
        person.setAddressTown("");
        person.setAddressPostcode("");
        this.person = person;
    }
}
