package com.rdg.rdg_2025.rdg_2025_spring.payload.response.person;

import com.rdg.rdg_2025.rdg_2025_spring.helpers.CreditFilter;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class PublicPersonResponse {

    private Person person;

    private PersonResponseType responseType = PersonResponseType.PUBLIC;

    private List<Credit> actingCredits;
    private List<Credit> musicianCredits;
    private List<Credit> producerCredits;

    public PublicPersonResponse(Person person) {
        person.setHomePhone("");
        person.setMobilePhone("");
        person.setAddressStreet("");
        person.setAddressTown("");
        person.setAddressPostcode("");

        this.person = person;

        this.actingCredits = CreditFilter.filterCreditTypes(person.getCredits(), CreditType.ACTOR);
        this.musicianCredits = CreditFilter.filterCreditTypes(person.getCredits(), CreditType.MUSICIAN);
        this.producerCredits = CreditFilter.filterCreditTypes(person.getCredits(), CreditType.PRODUCER);
    }
}
