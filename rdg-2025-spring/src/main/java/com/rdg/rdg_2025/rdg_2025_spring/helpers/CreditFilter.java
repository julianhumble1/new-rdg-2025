package com.rdg.rdg_2025.rdg_2025_spring.helpers;

import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;

import java.util.List;
import java.util.stream.Collectors;

public class CreditFilter {

    public static List<Credit> filterCreditTypes(List<Credit> creditList, CreditType typeToFilter) {
        return creditList.stream()
                .filter(credit -> credit.getType() == typeToFilter)
                .collect(Collectors.toList());
    }
}
