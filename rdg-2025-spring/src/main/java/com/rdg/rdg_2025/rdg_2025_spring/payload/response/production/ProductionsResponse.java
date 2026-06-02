package com.rdg.rdg_2025.rdg_2025_spring.payload.response.production;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProductionsResponse {

    private List<ProductionResponse> productions;

    public ProductionsResponse(List<Production> productions) {
        this.productions = productions.stream().map(ProductionResponse::new).toList();
    }


}
