package com.rdg.rdg_2025.rdg_2025_spring.repository;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Integer> {

    @Query(value = "SELECT COUNT(*) FROM productions p WHERE p.name LIKE BINARY :prefix%", nativeQuery = true)
    int countByNameStartingWith(@Param("prefix") String prefix);

    @Query(value = "SELECT p FROM Production p JOIN p.performances perf WHERE perf.time > :currentTime")
    List<Production> findAllProductionsWithFuturePerformances(@Param("currentTime") LocalDateTime currentTime);

}
