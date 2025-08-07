package com.example.telegram_api.model.request;

import lombok.Data;

@Data
public class SubjectRequest {
    private Long id;
    private String name;
    private Long classId;
}
