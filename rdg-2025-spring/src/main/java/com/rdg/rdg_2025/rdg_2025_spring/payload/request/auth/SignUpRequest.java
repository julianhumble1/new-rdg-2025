package com.rdg.rdg_2025.rdg_2025_spring.payload.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class SignUpRequest {

    @NotBlank
    @Size(min=3, max=20)
    private String username;

    @NotBlank
    @Size(max=50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min=6, max=40)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
