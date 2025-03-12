package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.credit.CreditRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.CreditRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    private ProductionService productionService;
    private PersonService personService;

    public CreditService(ProductionService productionService, PersonService personService) {
        this.productionService = productionService;
        this.personService = personService;
    }

    public void addNewCredit(CreditRequest creditRequest) {
        Production production = retrieveProductionFromService(creditRequest.getProductionId());
        Person person = retrievePersonFromService(creditRequest.getPersonId());

    }

    private Production retrieveProductionFromService(int productionId) {
        try {
            return productionService.getProductionById(productionId);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    private Person retrievePersonFromService(int personId) {
        if (personId > 0) {
            try {
                return personService.getPersonById(personId);
            } catch (DatabaseException ex) {
                throw new DatabaseException(ex.getMessage(), ex);
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException(ex.getMessage(),ex);
            }
        } else return null;
    }

}
