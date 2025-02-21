package com.rdg.rdg_2025.rdg_2025_spring.utils;

import com.rdg.rdg_2025.rdg_2025_spring.models.auth.ERole;
import com.rdg.rdg_2025.rdg_2025_spring.models.auth.Role;
import com.rdg.rdg_2025.rdg_2025_spring.models.auth.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.RoleRepository;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public class AuthTestUtils {

    public static User createTestAdmin(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        User testAdmin = new User();
        testAdmin.setUsername("test_admin");
        testAdmin.setEmail("admin@test.com");
        testAdmin.setPassword(passwordEncoder.encode("password123"));
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN) .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
        Set<Role> testAdminRoles = new HashSet<>();
        testAdminRoles.add(adminRole);
        testAdmin.setRoles(testAdminRoles);
        userRepository.save(testAdmin);
        return testAdmin;
    }

    public static User createTestUser(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        User testUser = new User();
        testUser.setUsername("test_user");
        testUser.setEmail("user@test.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        Role userRole = roleRepository.findByName(ERole.ROLE_USER) .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
        Set<Role> testUserRoles = new HashSet<>();
        testUserRoles.add(userRole);
        testUser.setRoles(testUserRoles);
        userRepository.save(testUser);
        return testUser;
    }

}
