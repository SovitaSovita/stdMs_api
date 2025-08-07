package com.example.telegram_api.model.response;

import com.example.telegram_api.model.entity.Exam;
import com.example.telegram_api.model.entity.Student;
import com.example.telegram_api.model.entity.Subject;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ScoreUpsertResponse {
    private Long id;
    private Double score;
    private Long studentId;
    private Long subjectId;
    private Long examId;

}
