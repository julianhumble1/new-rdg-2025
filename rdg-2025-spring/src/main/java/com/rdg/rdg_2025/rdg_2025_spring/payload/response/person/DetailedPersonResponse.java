package com.rdg.rdg_2025.rdg_2025_spring.payload.response.person;

import com.rdg.rdg_2025.rdg_2025_spring.helpers.CreditFilter;
import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class DetailedPersonResponse {

    private Person person;

    private PersonResponseType responseType = PersonResponseType.DETAILED;

    private List<Credit> actingCredits;
    private List<Credit> musicianCredits;
    private List<Credit> producerCredits;

    private List<Award> awards;

    public DetailedPersonResponse(Person person) {
        this.person = person;

        this.actingCredits = CreditFilter.filterCreditTypes(person.getCredits(), CreditType.ACTOR);
        this.musicianCredits = CreditFilter.filterCreditTypes(person.getCredits(), CreditType.MUSICIAN);
        this.producerCredits = CreditFilter.filterCreditTypes(person.getCredits(), CreditType.PRODUCER);
        this.awards = person.getAwards();
    }

}
