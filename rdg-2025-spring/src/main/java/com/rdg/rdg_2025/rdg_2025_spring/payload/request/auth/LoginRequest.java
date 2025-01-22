package com.rdg.rdg_2025.rdg_2025_spring.payload.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
