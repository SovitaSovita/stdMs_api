package com.example.telegram_api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;
    private String fatherName;
    private String fatherOccupation;
    private String montherName;
    private String montherOccupation;
    private String placeOfBirth;
    private String address;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private ClassRoom classRoom;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Score> scores = new ArrayList<>();
}