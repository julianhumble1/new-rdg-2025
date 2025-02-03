package com.rdg.rdg_2025.rdg_2025_spring.payload.response.production;

import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProductionResponse {

    private Production production;

    private List<Performance> performances;

    public ProductionResponse(Production production) {
        this.production = production;
        this.performances = production.getPerformances();
    }

}
