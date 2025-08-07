package com.example.telegram_api.model.request;

import com.example.telegram_api.model.entity.Exam;
import com.example.telegram_api.model.entity.Student;
import com.example.telegram_api.model.entity.Subject;
import com.example.telegram_api.model.entity.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomRequest {
    private Long id;
    private String name;
    private String grade;
    private String year;
//    private List<Student> students = new ArrayList<>();
//    private List<Subject> subjects = new ArrayList<>();
//    private List<Exam> exams = new ArrayList<>();
}
