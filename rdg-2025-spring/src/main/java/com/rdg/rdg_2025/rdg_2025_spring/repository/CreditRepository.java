package com.rdg.rdg_2025.rdg_2025_spring.repository;

import com.rdg.rdg_2025.rdg_2025_spring.models.credit.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Integer> {
}
