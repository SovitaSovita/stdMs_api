package com.example.telegram_api.authentication.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse{
    Integer userId;
    String username;
    String token;
}
