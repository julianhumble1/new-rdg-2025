package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals.NewFestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FestivalService {

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    VenueRepository venueRepository;

    public Festival addNewFestival(NewFestivalRequest newFestivalRequest) {

        Venue venue = new Venue();

        if (newFestivalRequest.getVenueId() != 0) {
            venue = venueRepository.findById(newFestivalRequest.getVenueId())
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + newFestivalRequest.getVenueId()));
        }

        return new Festival();
    }

}
