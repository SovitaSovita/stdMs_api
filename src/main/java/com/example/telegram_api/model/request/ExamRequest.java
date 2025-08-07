package com.example.telegram_api.model.request;

import com.example.telegram_api.model.entity.ClassRoom;
import com.example.telegram_api.model.entity.ExamType;
import com.example.telegram_api.model.entity.Score;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExamRequest {
    private Long id;
    private String title;
    private ExamType examType = ExamType.MONTHLY;
    private LocalDate examDate;
    private Long classId;
}
