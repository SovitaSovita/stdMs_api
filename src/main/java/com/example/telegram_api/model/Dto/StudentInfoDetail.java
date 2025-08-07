package com.example.telegram_api.model.Dto;

import com.example.telegram_api.model.response.StudentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfoDetail {
    List<StudentResponse> student;
    private Integer total;
    private Integer totalMale;
    private Integer totalFemale;
}

