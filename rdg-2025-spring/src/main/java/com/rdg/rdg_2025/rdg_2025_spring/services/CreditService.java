package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
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

    public void addNewCredit(CreditRequest creditRequest) {
        Credit credit = new Credit();

        try {
            creditRepository.save(credit);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }
}
