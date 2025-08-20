package com.rdg.rdg_2025.rdg_2025_spring.payload.response.award;

import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AwardResponse {
    private Award award;

    public AwardResponse(Award award) {
        this.award = award;
    }
}
