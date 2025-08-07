package com.example.telegram_api.model.Dto;

import com.example.telegram_api.model.entity.ExamType;
import com.example.telegram_api.model.entity.Score;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExamFilterScoreDto {
    private Long id;
    private String name;
    private Double score;
}
