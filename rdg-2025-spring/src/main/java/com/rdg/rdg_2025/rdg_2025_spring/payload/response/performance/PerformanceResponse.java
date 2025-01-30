package com.rdg.rdg_2025.rdg_2025_spring.payload.response.performance;

import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PerformanceResponse {

    private Performance performance;

    public PerformanceResponse(Performance performance) {
        this.performance = performance;
    }
}
