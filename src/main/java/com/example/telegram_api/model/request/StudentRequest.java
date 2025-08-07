package com.example.telegram_api.model.request;

import com.example.telegram_api.model.entity.ClassRoom;
import com.example.telegram_api.model.entity.Gender;
import com.example.telegram_api.model.entity.Score;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentRequest {
    private Long id;
    private Long classId;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
//    private List<Score> scores = new ArrayList<>();
}
