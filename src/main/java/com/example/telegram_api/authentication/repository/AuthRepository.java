package com.example.telegram_api.authentication.repository;
import com.example.telegram_api.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthRepository extends JpaRepository<Users, UUID> {
    Users findByUsername(String username);
}
