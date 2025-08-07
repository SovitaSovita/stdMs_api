package com.example.telegram_api.model.response;

import com.example.telegram_api.model.Dto.ExamUpsertResponse;
import com.example.telegram_api.model.Dto.SubjectUpsertResponse;
import com.example.telegram_api.model.entity.Exam;
import com.example.telegram_api.model.entity.Student;
import com.example.telegram_api.model.entity.Subject;
import com.example.telegram_api.model.entity.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassResponse {
    private Long id;
    private String name;
    private String grade;
    private String year;
    private UserResponse user;
    private List<StudentResponse> students = new ArrayList<>();
    private List<SubjectUpsertResponse> subjects = new ArrayList<>();
    private List<ExamUpsertResponse> exams = new ArrayList<>();
}
