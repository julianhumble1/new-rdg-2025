package com.rdg.rdg_2025.rdg_2025_spring.payload.request.performance;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @ToString
public class PerformanceRequest {

    @Min(1)
    int productionId;

    @Min(1)
    int venueId;

    int festivalId;

    @NotNull
    LocalDateTime time;

    String description;
    BigDecimal standardPrice;
    BigDecimal concessionPrice;
    String boxOffice;

    public PerformanceRequest(int productionId, int venueId, int festivalId, LocalDateTime time,  String description, BigDecimal standardPrice, BigDecimal concessionPrice, String boxOffice) {
        this.productionId = productionId;
        this.venueId = venueId;
        this.festivalId = festivalId;
        this.time = time;
        this.description = description;
        this.standardPrice = standardPrice;
        this.concessionPrice = concessionPrice;
        this.boxOffice = boxOffice;
    }

}
