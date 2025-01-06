package com.rdg.rdg_2025.rdg_2025_spring.repository;

import com.rdg.rdg_2025.rdg_2025_spring.models.ERole;
import com.rdg.rdg_2025.rdg_2025_spring.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
