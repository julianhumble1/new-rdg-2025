package com.rdg.rdg_2025.rdg_2025_spring.payload.response.production;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductionResponse {

    private Production production;

    public ProductionResponse(Production production) {
        this.production = production;
    }

}
