package com.example.telegram_api.authentication.repository;

import com.example.telegram_api.model.entity.Exam;
import com.example.telegram_api.model.entity.Score;
import com.example.telegram_api.model.entity.Student;
import com.example.telegram_api.model.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Boolean existsByStudentAndSubjectAndExam(Student student, Subject subject, Exam exam);
    Score findByStudentIdAndSubjectIdAndExamId(Long student, Long subjectId, Long examId);
}
