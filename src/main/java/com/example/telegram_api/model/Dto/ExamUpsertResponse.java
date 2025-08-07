package com.example.telegram_api.model.Dto;

import com.example.telegram_api.model.entity.ExamType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamUpsertResponse {
    private Long id;
    private String title;
    private ExamType examType;
    private LocalDate examDate;
    private Long classId;
}
