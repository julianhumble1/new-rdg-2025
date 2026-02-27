package com.rdg.rdg_2025.rdg_2025_spring.payload.response.auth;

import com.rdg.rdg_2025.rdg_2025_spring.models.auth.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UsersResponse {

    private List<User> users;

    public UsersResponse(List<User> users) {
        this.users = users;
    }
}
