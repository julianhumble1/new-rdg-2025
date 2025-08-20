package com.rdg.rdg_2025.rdg_2025_spring.payload.request.award;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class AwardRequest {

    @NotBlank
    private String name;

    private int productionId;

    private int personId;

    private int festivalId;

    public AwardRequest(String name, int productionId, int personId, int festivalId) {
        this.name = name;
        this.productionId = productionId;
        this.personId = personId;
        this.festivalId = festivalId;
    }
}
