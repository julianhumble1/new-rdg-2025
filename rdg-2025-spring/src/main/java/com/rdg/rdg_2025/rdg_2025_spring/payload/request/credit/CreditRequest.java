package com.rdg.rdg_2025.rdg_2025_spring.payload.request.credit;

import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CreditRequest {

    @NotNull
    private String name;

    @NotNull
    private CreditType type;

    @Min(1)
    private int productionId;

    private String summary;
    private int personId;

    public CreditRequest(String name, CreditType type, int productionId, String summary, int personId) {
        this.name = name;
        this.type = type;
        this.productionId = productionId;
        this.summary = summary;
        this.personId = personId;
    }
}
