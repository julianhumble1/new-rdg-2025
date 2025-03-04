package com.rdg.rdg_2025.rdg_2025_spring.repository;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Integer> {

    @Query(value = "SELECT COUNT(*) FROM productions p WHERE p.name LIKE BINARY :prefix%", nativeQuery = true)
    int countByNameStartingWith(@Param("prefix") String prefix);

}
