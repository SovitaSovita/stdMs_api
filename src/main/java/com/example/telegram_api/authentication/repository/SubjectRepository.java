package com.example.telegram_api.authentication.repository;

import com.example.telegram_api.model.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Subject findByIdAndClassRoomId(Long id, Long classRoomId);
    List<Subject> findByClassRoomId(Long classRoomId);
}
