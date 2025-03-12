package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import com.rdg.rdg_2025.rdg_2025_spring.models.credit.CreditType;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.credit.CreditRequest;
import com.rdg.rdg_2025.rdg_2025_spring.repository.CreditRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private ProductionService productionService;

    @InjectMocks
    private CreditService creditService;

    @Nested
    @DisplayName("addNewCredit service tests")
    class AddNewCreditServiceTests {

        private CreditRequest creditRequest;

        @BeforeEach
        void setup() {
            creditRequest = new CreditRequest(
                    "Test Credit",
                    CreditType.ACTOR,
                    1,
                    "Test Summary",
                    1
            );
        }

        @Test
        void TestProductionServiceMethodIsCalled() {
            // Arrange
            // Act
            creditService.addNewCredit(creditRequest);
            // Assert
            verify(productionService, times(1)).getProductionById(anyInt());
        }



    }
}
