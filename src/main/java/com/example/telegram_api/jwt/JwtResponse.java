package com.example.telegram_api.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private final LocalDateTime dateTime;
    private final String username;
    private final String token;
    private final String role;
    private final String fullname;
    private final String profile;
    private final String email;
}
