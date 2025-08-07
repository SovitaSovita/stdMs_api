package com.example.telegram_api.model.Dto;

import lombok.Data;

import java.util.Map;

@Data
public class SubjectUpsertResponse {
    private Long id;
    private String name;
    private Long classId;
}
