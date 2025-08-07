package com.example.telegram_api.authentication.repository;
import com.example.telegram_api.model.entity.ClassRoom;
import com.example.telegram_api.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassroomRepository extends JpaRepository<ClassRoom, Long> {
    List<ClassRoom> findByUserId(UUID userId);
    ClassRoom findByIdAndUserId(Long classroomId, UUID userId);
}
