package com.example.telegram_api.authentication.controller;

import com.example.telegram_api.authentication.repository.ClassroomRepository;
import com.example.telegram_api.authentication.service.ClassroomServiceImpl;
import com.example.telegram_api.model.Dto.StudentInfoDetail;
import com.example.telegram_api.model.entity.ClassRoom;
import com.example.telegram_api.model.entity.ExamType;
import com.example.telegram_api.model.request.*;
import com.example.telegram_api.model.response.ApiResponse;
import com.example.telegram_api.model.response.ClassInfoResponse;
import com.example.telegram_api.model.response.ClassResponse;
import com.example.telegram_api.model.response.ScoreUpsertResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
//@Tag(name = "Classroom", description = "classroom crud")
@CrossOrigin
@SecurityRequirement(name = "bearerAuth")
public class ClassroomController {

    private final ClassroomServiceImpl classroomService;

    public ClassroomController(ClassroomServiceImpl classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping("/class")
    @Operation(summary = "upsert classroom 🤵")
    public ApiResponse<?> createClassroom(@RequestBody ClassRoomRequest classroom) {
        String msg = "";
        if(classroom.getId() != null) msg = "updated";
        else msg = "created";

        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .payload(classroomService.createClassroom(classroom))
                .message("classroom has been " + msg)
                .build();
    }
    @GetMapping("/classes")
    @Operation(summary = "Get list by current user 🤵")
    public List<ClassResponse>  getClassByUserId() {
        return classroomService.getClassroomByUserId();
    }

    @GetMapping("/info-classes")
    @Operation(summary = "Get classes info🤵")
    public List<ClassInfoResponse>  getClassInfoByUserId() {
        return classroomService.getClassroomInfoByUserId();
    }

    @PostMapping("/class/{classId}/filter")
    @Operation(summary = "Get by classId 🤵")
    public Object getClassById(@PathVariable Long classId,
                                       @RequestBody(required = false) ClassroomExamFilterRequest filterRequest) {
        return classroomService.getClassById(classId, filterRequest);
    }

    @DeleteMapping("/class/{classId}")
    public ApiResponse<?> removeClassById(@PathVariable Long classId) {
        classroomService.removeClassroomById(classId);
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .message("classroom have been removed")
                .build();
    }

    @PostMapping("/student")
    @Operation(summary = "upsert student 🤵")
    public ApiResponse<?> createStudent(@RequestBody StudentRequest stuRequest) {
        String msg = "";
        if(stuRequest.getId() != null) msg = "updated";
        else msg = "created";
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .payload(classroomService.upsertStudent(stuRequest))
                .message("student has been " + msg)
                .build();
    }

    @GetMapping("/students/{classId}")
    @Operation(summary = "Get list student by classId 🤵")
    public StudentInfoDetail getStudentsByClassId(@PathVariable Long classId) {
        return classroomService.getStudentsByClassId(classId);
    }

    @DeleteMapping("/student/{stuId}")
    public ApiResponse<?> removeStudentById(@PathVariable Long stuId) {
        classroomService.removeStuById(stuId);
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .message("student have been removed")
                .build();
    }


    @PostMapping("/subject")
    @Operation(summary = "upsert subject of class 🤵")
    public ApiResponse<?> createOrModifyScore(@RequestBody SubjectRequest subRequest) {
        String msg = "";
        if(subRequest.getId() != null) msg = "updated";
        else msg = "created";
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .payload(classroomService.upsertSubjectOfClass(subRequest))
                .message("subject has been " + msg)
                .build();
    }

    @DeleteMapping("/subject/{subId}")
    public ApiResponse<?> removeSubById(@PathVariable Long subId) {
        classroomService.removeSubjectById(subId);
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .message("subject have been removed")
                .build();
    }


    @PostMapping("/exam")
    @Operation(summary = "upsert exam of subject 🤵")
    public ApiResponse<?> createOrModifyExam(@RequestBody ExamRequest examRequest) {
        String msg = "";
        if(examRequest.getId() != null) msg = "updated";
        else msg = "created";
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .payload(classroomService.upsertExamOfClass(examRequest))
                .message("exam has been " + msg)
                .build();
    }

    @PostMapping("/scores/{classId}/{examId}")
    @Operation(summary = "upsert score of exam 🤵")
    public List<ScoreUpsertResponse> createOrModifyScore(@RequestBody List<ScoreUpsertRequest> requests,
                                                         @PathVariable Long examId,
                                                         @PathVariable Long classId) {
        return classroomService.upsertScoreOfExam(requests, examId , classId);
    }

    @DeleteMapping("/exam/{examId}")
    public ApiResponse<?> removeExamById(@PathVariable Long examId) {
        classroomService.removeExamAndScoreById(examId);
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .status(200)
                .message("exam and scores have been removed")
                .build();
    }
}
