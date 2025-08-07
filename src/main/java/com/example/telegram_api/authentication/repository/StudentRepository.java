package com.example.telegram_api.authentication.repository;

import com.example.telegram_api.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByClassRoomId(Long classRoomId);
    Student findByIdAndClassRoomId(Long id, Long classRoomId);
}
