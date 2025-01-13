package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.ProductionRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductionServiceTest {

    @InjectMocks
    private ProductionService productionService;

    @Mock
    private ProductionRepository productionRepository;

    @Mock
    private VenueRepository venueRepository;

    @Nested
    @DisplayName("addNewProduction service tests")
    class addNewProductionServiceTests {

        @Test
        void testInvalidVenueIdThrowsEntityNotFoundException() {
            // Arrange
            when(venueRepository.findById(any())).thenReturn(Optional.empty());
            NewProductionRequest productionRequest = new NewProductionRequest(
                    "Test Request",
                    1,
                    "Test Author",
                    "Test Description",
                    LocalDateTime.now(),
                    false,
                    false,
                    "Test File String"
                    );

            // Act & Assert
            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
                productionService.addNewProduction(productionRequest);
            });

        }

    }

}
