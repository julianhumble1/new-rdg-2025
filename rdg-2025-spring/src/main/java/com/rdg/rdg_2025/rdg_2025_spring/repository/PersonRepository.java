package com.rdg.rdg_2025.rdg_2025_spring.repository;

import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
