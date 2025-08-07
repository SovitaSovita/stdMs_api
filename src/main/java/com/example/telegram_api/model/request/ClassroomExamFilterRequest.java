package com.example.telegram_api.model.request;

import com.example.telegram_api.model.entity.ExamType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClassroomExamFilterRequest {
    private ExamType examType;
    private LocalDate examDate;
}
