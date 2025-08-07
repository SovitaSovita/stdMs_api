package com.example.telegram_api.model.response;

import com.example.telegram_api.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String fullname;
    private String profile;
    private Role role;
}
