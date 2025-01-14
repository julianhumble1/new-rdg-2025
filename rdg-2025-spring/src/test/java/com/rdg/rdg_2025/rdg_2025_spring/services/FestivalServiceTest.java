package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.festivals.NewFestivalRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.FestivalRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FestivalServiceTest {

    @InjectMocks
    FestivalService festivalService;

    @Mock
    FestivalRepository festivalRepository;

    @Mock
    VenueRepository venueRepository;

    private NewFestivalRequest testNewFestivalRequest = new NewFestivalRequest(
            "Test Festival", 1, 2025, 4, "Test Description"
    );

    private Festival testFestival = new Festival(
            "Test Festival", new Venue(), 2025, 4, "Test Description"
    );

    @Nested
    @DisplayName("addNewFestival service tests")
    class addNewFestivalServiceTests{

        @Test
        void testInvalidVenueIdThrowsEntityNotFoundException() {
            // Arrange
            when(venueRepository.findById(any())).thenThrow(EntityNotFoundException.class);

            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                festivalService.addNewFestival(testNewFestivalRequest);
            });
        }

        @Test
        void testNewFestivalWithFullDetailsIncludingVenueReturnsFestivalObject() {
            // Arrange
            Venue testVenue = new Venue();
            when(venueRepository.findById(any())).thenReturn(Optional.of(testVenue));

            when(festivalRepository.save(any(Festival.class))).thenReturn(testFestival);

            // Act
            Festival result = festivalService.addNewFestival(testNewFestivalRequest);
            // Assert
            assertEquals(testFestival, result);

        }

    }

}
