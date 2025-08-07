package com.example.telegram_api.model.request;
import lombok.Data;

@Data
public class ScoreUpsertRequest {
    private Double score;
    private Long studentId;
    private Long subjectId;
}
