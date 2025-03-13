package com.rdg.rdg_2025.rdg_2025_spring.payload.response.production;

import com.rdg.rdg_2025.rdg_2025_spring.helpers.CreditFilter;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProductionResponse {

    private Production production;

    private List<Performance> performances;

    private List<Credit> actingCredits;
    private List<Credit> musicianCredits;
    private List<Credit> producerCredits;

    public ProductionResponse(Production production) {
        this.production = production;
        this.performances = production.getPerformances();
        this.actingCredits = CreditFilter.filterCreditTypes(production.getCredits(), CreditType.ACTOR);
        this.musicianCredits = CreditFilter.filterCreditTypes(production.getCredits(), CreditType.MUSICIAN);
        this.producerCredits = CreditFilter.filterCreditTypes(production.getCredits(), CreditType.PRODUCER);
    }



}
