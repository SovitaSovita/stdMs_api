package com.example.telegram_api.model.response;

import com.example.telegram_api.model.Dto.ExamUpsertResponse;
import com.example.telegram_api.model.Dto.StudentScoreResponse;
import com.example.telegram_api.model.entity.Score;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ClassExamFilterResponse {
    private Long id;
    private String name;
    private String grade;
    private String year;
//    private UserResponse user;
    private ExamUpsertResponse exams;
    private List<StudentScoreResponse> students;
}
