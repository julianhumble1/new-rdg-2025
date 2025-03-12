package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.credit.CreditRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.CreditRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    private ProductionService productionService;

    public CreditService(ProductionService productionService) {
        this.productionService = productionService;
    }

    public void addNewCredit(CreditRequest creditRequest) {
        Production production = productionService.getProductionById(creditRequest.getProductionId());


    }

}
