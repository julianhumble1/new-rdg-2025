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

import java.time.LocalDateTime;

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

    // PUBLIC CRUD METHODS

    public Credit addNewCredit(CreditRequest creditRequest) {
        Production production = retrieveProductionFromService(creditRequest.getProductionId());
        Person person = retrievePersonFromService(creditRequest.getPersonId());
        Credit credit = new Credit();

        updateCreditFromRequest(credit, creditRequest, production, person);

        return saveCreditToDatabase(credit);

    }

    public Credit getCreditById(int creditId) {
        try {
            return creditRepository.findById(creditId)
                .orElseThrow(() -> new EntityNotFoundException("No Credit with this id: " + creditId));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    public Credit updateCredit(int creditId, CreditRequest creditRequest) {
        Credit credit = getCreditById(creditId);
        Production production = retrieveProductionFromService(creditRequest.getProductionId());
        Person person = retrievePersonFromService(creditRequest.getPersonId());

        updateCreditFromRequest(credit, creditRequest, production, person);

        return saveCreditToDatabase(credit);

    }

    public void deleteCreditById(int creditId) {
        Credit credit = getCreditById(creditId);

        creditRepository.delete(credit);
    }

    // PRIVATE HELPER METHODS

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

    private Credit saveCreditToDatabase(Credit credit) {
        try {
            return creditRepository.save(credit);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    private void updateCreditFromRequest(Credit credit, CreditRequest creditRequest, Production production, Person person) {
        credit.setName(creditRequest.getName());
        credit.setType(creditRequest.getType());
        credit.setSummary(creditRequest.getSummary());
        credit.setPerson(person);
        credit.setProduction(production);
        if (credit.getCreatedAt() == null) {
            credit.setCreatedAt(LocalDateTime.now());
        }
        credit.setUpdatedAt(LocalDateTime.now());


    }

}
