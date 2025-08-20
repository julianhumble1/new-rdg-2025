package com.rdg.rdg_2025.rdg_2025_spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.award.AwardRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.AwardRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;

@Service
public class AwardService {

    @Autowired
    private AwardRepository awardRepository;

    private FestivalService festivalService;
    private PersonService personService;
    private ProductionService productionService;

    public AwardService(FestivalService festivalService,
            PersonService personService,
            ProductionService productionService) {
        this.festivalService = festivalService;
        this.personService = personService;
        this.productionService = productionService;
    }

    // PUBLIC CRUD METHODS

    public Award addNewAward(AwardRequest awardRequest) {
        Festival festival = retrieveFestivalFromService(awardRequest.getFestivalId());
        Person person = retrievePersonFromService(awardRequest.getPersonId());
        Production production = retrieveProductionFromService(awardRequest.getProductionId());

        Award award = new Award();
        updateAwardFromRequest(award, awardRequest, festival, person, production);

        return saveAwardToDatabase(award);
    }

    public Award getAwardById(int awardId) {
        try {
            return awardRepository.findById(awardId)
                    .orElseThrow(() -> new EntityNotFoundException("No Award with this id: " + awardId));
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    public Award updateAward(int awardId, AwardRequest awardRequest) {
        Award award = getAwardById(awardId);
        Festival festival = retrieveFestivalFromService(awardRequest.getFestivalId());
        Person person = retrievePersonFromService(awardRequest.getPersonId());
        Production production = retrieveProductionFromService(awardRequest.getProductionId());

        updateAwardFromRequest(award, awardRequest, festival, person, production);

        return saveAwardToDatabase(award);
    }

    public void deleteAwardById(int awardId) {
        Award award = getAwardById(awardId);
        deleteAwardInDatabase(award);
    }

    // PRIVATE HELPERS

    private Festival retrieveFestivalFromService(int festivalId) {
        try {
            return festivalService.getFestivalById(festivalId);
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
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException(ex.getMessage(), ex);
            } catch (DataAccessException ex) {
                throw new DatabaseException(ex.getMessage(), ex);
            }
        } else {
            return null;
        }
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

    private Award saveAwardToDatabase(Award award) {
        try {
            return awardRepository.save(award);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    private void updateAwardFromRequest(Award award, AwardRequest req, Festival festival, Person person,
            Production production) {
        award.setName(req.getName());
        award.setFestival(festival);
        award.setPerson(person);
        award.setProduction(production);
    }

    private void deleteAwardInDatabase(Award award) {
        try {
            awardRepository.delete(award);
        } catch (DataAccessException | PersistenceException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }
}