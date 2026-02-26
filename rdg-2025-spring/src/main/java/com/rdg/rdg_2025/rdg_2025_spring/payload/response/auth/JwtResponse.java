package com.rdg.rdg_2025.rdg_2025_spring.payload.response.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private boolean isPasswordResetRequired;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, boolean isPasswordResetRequired, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.isPasswordResetRequired = isPasswordResetRequired;
        this.roles = roles;
    }
}
