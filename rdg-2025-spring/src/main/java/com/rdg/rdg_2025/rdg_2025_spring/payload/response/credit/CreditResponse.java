package com.rdg.rdg_2025.rdg_2025_spring.payload.response.credit;

import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreditResponse {

    private Credit credit;

    public CreditResponse(Credit credit) {
        this.credit = credit;
    }
}
