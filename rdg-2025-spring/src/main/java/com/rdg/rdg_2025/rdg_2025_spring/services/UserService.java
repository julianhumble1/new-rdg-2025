package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.auth.User;
import com.rdg.rdg_2025.rdg_2025_spring.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public void resetUserPassword(int userId, String newPassword) {
        try {
            User user = userRepository.findById((long) userId)
                    .orElseThrow(() -> new EntityNotFoundException("No user with this id"));
            updatePassword(user, newPassword, true);
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    public void updateOwnPassword(String username, String newPassword) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("No user with this username"));
            updatePassword(user, newPassword, false);
        } catch (DataAccessException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    private void updatePassword(User user, String newPassword, boolean passwordResetRequired) {
        user.setPassword(encoder.encode(newPassword));
        user.setPasswordResetRequired(passwordResetRequired);
        userRepository.save(user);
    }

}
