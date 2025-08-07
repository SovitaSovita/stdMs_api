package com.example.telegram_api.model.Dto;

import com.example.telegram_api.model.entity.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class StudentScoreResponse {
    private Long id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Double totalScore;
    private Map<String, Double> scores;
}
