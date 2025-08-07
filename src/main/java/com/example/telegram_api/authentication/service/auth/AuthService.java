package com.example.telegram_api.authentication.service.auth;


import com.example.telegram_api.authentication.auth.InfoChangePassword;
import com.example.telegram_api.model.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AuthService {
    Users getUserByUsername(String username);
    void changePassword(UUID id, InfoChangePassword password);
    Users getCurrentUser();
}
