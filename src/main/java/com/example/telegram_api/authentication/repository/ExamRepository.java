package com.example.telegram_api.authentication.repository;

import com.example.telegram_api.model.entity.Exam;
import com.example.telegram_api.model.entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam,Long> {
    @Query("SELECT e FROM Exam e WHERE MONTH(e.examDate) = :month AND YEAR(e.examDate) = :year AND e.examType = :examType AND e.classRoom.id = :classId")
    Optional<Exam> findByMonthAndYearAndTypeAndClassId(@Param("month") int month,
                                                       @Param("year") int year,
                                                       @Param("examType") ExamType examType,
                                                       @Param("classId") Long classId);



    @Query(
            value = """
                select * from exam ex
                 LEFT JOIN scores sc ON exam_id = 3
                 where ex.exam_date='2025-07-23' and ex.exam_type='MONTHLY'
        """,
            nativeQuery = true
    )
    List<Object> findExamWithScoresNative(
            @Param("examId") Long examId,
            @Param("examDate") LocalDate examDate,
            @Param("examType") ExamType examType
    );

    Exam findByIdAndClassRoomId(Long id, Long classRoomId);
    List<Exam> findByClassRoomId(Long classRoomId);

}
