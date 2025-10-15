package com.example.telegram_api.authentication.service;

import com.example.telegram_api.authentication.repository.*;
import com.example.telegram_api.authentication.service.auth.AuthServiceImpl;
import com.example.telegram_api.common.CommonUtils;
import com.example.telegram_api.exception.NotFoundExceptionClass;
import com.example.telegram_api.model.Dto.*;
import com.example.telegram_api.model.entity.*;
import com.example.telegram_api.model.request.*;
import com.example.telegram_api.model.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl {
    private final ClassroomRepository classroomRepository;
    private final AuthServiceImpl authService;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ExamRepository examRepository;
    private final ScoreRepository scoreRepository;
    private final ModelMapper mapper;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, AuthServiceImpl authService, StudentRepository studentRepository, SubjectRepository subjectRepository, ExamRepository examRepository, ScoreRepository scoreRepository, ModelMapper mapper) {
        this.classroomRepository = classroomRepository;
        this.authService = authService;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.examRepository = examRepository;
        this.scoreRepository = scoreRepository;
        this.mapper = mapper;
    }

    public ClassResponse createClassroom(ClassRoomRequest classroom) {
        if (classroom.getName().isBlank() || classroom.getGrade().isBlank())
            throw new IllegalArgumentException("Name and Grade cannot be empty");
        if (!CommonUtils.isInteger(classroom.getGrade()))
            throw new IllegalArgumentException("Grade must be a number");
        //Validation Year
        checkYearValidation(classroom.getYear());

        Users user = authService.getCurrentUser();
        ClassRoom classroomEntity = new ClassRoom();

        if (classroom.getId() != null) {
            ClassRoom classroom1 = classroomRepository.findByIdAndUserId(classroom.getId(), user.getId());
            if (classroom1 == null) {
                throw new NotFoundExceptionClass("class with id: " + classroom.getId() + " not found in user: " + user.getUsername());
            }
            classroomEntity.setId(classroom1.getId());
        }
        classroomEntity.setName(classroom.getName());
        classroomEntity.setGrade(classroom.getGrade());

        classroomEntity.setYear(classroom.getYear());
        classroomEntity.setUser(user);
//      classroomEntity.setStudents(classroomEntity.getStudents());
//      classroomEntity.setSubjects(classroomEntity.getSubjects());
//      classroomEntity.setExams(classroomEntity.getExams());
        ClassRoom classRoom = classroomRepository.save(classroomEntity);
        return mapper.map(classRoom, ClassResponse.class);
    }

    private void checkYearValidation(String year) {
        if (year.isBlank() || !CommonUtils.isInteger(year))
            throw new IllegalArgumentException("Invalid year");
        if (!CommonUtils.isValidYear(year))
            throw new IllegalArgumentException("Year must be a valid 4-digit year between 2020 and " + Year.now().getValue());
    }

    public List<ClassResponse> getClassroomByUserId() {
        Users user = authService.getCurrentUser();
        List<ClassRoom> classRooms = classroomRepository.findByUserId(user.getId());
        return classRooms.stream()
                .map(classRoom -> mapper.map(classRoom, ClassResponse.class))
                .toList();
    }

    public Object getClassById(Long classId, ClassroomExamFilterRequest filterRequest) {

        System.out.println("filterRequest" + filterRequest);
        ClassRoom room = classroomRepository.findByIdAndUserId(classId, authService.getCurrentUser().getId());
        if (room == null)
            throw new NotFoundExceptionClass("class with id: " + classId + " not found in  user: " + authService.getCurrentUser().getUsername());

        if (filterRequest == null) {
            return mapper.map(room, ClassResponse.class);
        }

        // Extract month, year, and examType from request
        int month = filterRequest.getExamDate().getMonthValue();
        int year = filterRequest.getExamDate().getYear();
        ExamType type = filterRequest.getExamType();

        // Find matching exam
        Exam matchedExam = room.getExams().stream()
                .filter(e -> e.getExamDate().getMonthValue() == month &&
                        e.getExamDate().getYear() == year &&
                        e.getExamType() == type)
                .findFirst()
                .orElseThrow(() -> new NotFoundExceptionClass("No exam found for " + month + "-" + year + " with type: " + type));

        // Prepare student scores
        List<StudentScoreResponse> studentScores = room.getStudents().stream().map(student -> {
            StudentScoreResponse studentRes = new StudentScoreResponse();
            studentRes.setId(student.getId());
            studentRes.setFullName(student.getFullName());
            studentRes.setGender(student.getGender());
            studentRes.setDateOfBirth(student.getDateOfBirth());

            Map<String, Double> subjectScores = new HashMap<>();

            matchedExam.getScores().stream()
                    .filter(score -> score.getStudent().getId().equals(student.getId()))
                    .forEach(score -> subjectScores.put(score.getSubject().getName(), score.getScore()));

            // Sum the total score from the map
            double totalScore = subjectScores.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
            studentRes.setTotalScore(totalScore);
            studentRes.setScores(subjectScores);
            return studentRes;
        }).toList();

        // Build the final response
        ClassExamFilterResponse response = new ClassExamFilterResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setGrade(room.getGrade());
        response.setYear(room.getYear());
        response.setExams(mapper.map(matchedExam, ExamUpsertResponse.class));

        // You can adjust to return a list if needed
        // For now, assume only one student's score is returned if needed
        // or wrap in a list if `ClassExamFilterResponse` is changed
        response.setStudents(studentScores);
        return response;
    }

    public void removeClassroomById(Long classId) {
        ClassRoom room = classroomRepository.findByIdAndUserId(classId, authService.getCurrentUser().getId());
        if (room != null) {
            classroomRepository.deleteById(classId);
        }
    }

    private void autoFillScoreOfStudentInsert(ClassRoom classRoom, Student student) {
        List<Exam> examExist = classRoom.getExams();

        for (Exam exam : examExist) {
            for(Subject subject : classRoom.getSubjects()){
                Score score = new Score();
                score.setId(exam.getId());
                score.setSubject(subject);
                score.setStudent(student);
                score.setExam(exam);
                score.setScore(0.0);
                scoreRepository.save(score);
            }
        }
    }

    public Object upsertStudent(StudentRequest stuRequest) {

        if (stuRequest.getFullName().isBlank())
            throw new IllegalArgumentException("Full Name cannot be empty");
        if (stuRequest.getGender() == null)
            throw new IllegalArgumentException("Gender must be 'M' or 'F'");

        ClassRoom room = classroomRepository.findByIdAndUserId(stuRequest.getClassId(), authService.getCurrentUser().getId());
        if (room == null)
            throw new NotFoundExceptionClass("class with id: " + stuRequest.getClassId() + " not found in user id: " + authService.getCurrentUser().getUsername());

        Student student = new Student();
        student.setClassRoom(room);
        student.setGender(stuRequest.getGender());
        student.setFullName(stuRequest.getFullName());
        student.setDateOfBirth(stuRequest.getDateOfBirth());
        student.setFatherName(stuRequest.getFatherName());
        student.setMontherName(stuRequest.getMontherName());
        student.setFatherOccupation(stuRequest.getFatherOccupation());
        student.setMontherOccupation(stuRequest.getMontherOccupation());
        student.setAddress(stuRequest.getAddress());
        student.setPlaceOfBirth(stuRequest.getPlaceOfBirth());

        Student newStudent = null;
        
        if (stuRequest.getId() != null) {
            Student existStu = studentRepository.findById(stuRequest.getId())
                    .orElseThrow(() -> new NotFoundExceptionClass("student with id: " + stuRequest.getId() + " not found"));
            student.setId(existStu.getId());
            newStudent = studentRepository.save(student);
        }
        else{
             newStudent = studentRepository.save(student);
             if(!room.getExams().isEmpty()){
                 autoFillScoreOfStudentInsert(room, newStudent);
             }
        }
        return mapper.map(newStudent, StudentResponse.class);
    }

    public StudentInfoDetail getStudentsByClassId(Long classId) {
        StudentInfoDetail studentInfoDetail = new StudentInfoDetail();

        List<Student> students = studentRepository.findByClassRoomId(classId);
        List<StudentResponse> studentResponseList = students.stream()
                .map(student -> mapper.map(student, StudentResponse.class)).toList();
        studentInfoDetail.setStudent(studentResponseList);

        studentInfoDetail.setTotal(studentResponseList.size());
        studentInfoDetail.setTotalMale((int) studentResponseList.stream()
                .filter(stu -> Gender.M.equals(stu.getGender())).count());
        studentInfoDetail.setTotalFemale(studentInfoDetail.getTotal() - studentInfoDetail.getTotalMale());

        return studentInfoDetail;
    }

    public void removeStuById(Long stuId) {
        studentRepository.findById(stuId)
                .orElseThrow(() -> new NotFoundExceptionClass("student with id: " + stuId + " not found"));
        studentRepository.deleteById(stuId);
    }

    public Object upsertSubjectOfClass(SubjectRequest subRequest) {

        ClassRoom classroom = classroomRepository.findByIdAndUserId(
                subRequest.getClassId(), authService.getCurrentUser().getId());

        if (classroom == null)
            throw new NotFoundExceptionClass("class with id: " + subRequest.getClassId() + " not found");

        Subject subject;
        boolean isNewSubject = (subRequest.getId() == null);

        if (isNewSubject) {
            subject = new Subject();
        } else {
            subject = subjectRepository.findById(subRequest.getId())
                    .orElseThrow(() -> new NotFoundExceptionClass("subject with id: " + subRequest.getId() + " not found"));
        }

        subject.setName(subRequest.getName());
        subject.setClassRoom(classroom);
        Subject savedSubject = subjectRepository.save(subject);

        // If new subject, create default scores for all students in classroom and all exams
        if (isNewSubject) {
            List<Student> students = studentRepository.findByClassRoomId(classroom.getId());
            List<Exam> exams = examRepository.findByClassRoomId(classroom.getId());

            List<Score> defaultScores = new ArrayList<>();

            for (Student student : students) {
                for (Exam exam : exams) {
                    boolean exists = scoreRepository.existsByStudentAndSubjectAndExam(student, savedSubject, exam);
                    if (!exists) {
                        Score score = new Score();
                        score.setStudent(student);
                        score.setSubject(savedSubject);
                        score.setExam(exam);
                        score.setScore(0.0);
                        defaultScores.add(score);
                    }
                }
            }

            if (!defaultScores.isEmpty()) {
                scoreRepository.saveAll(defaultScores);
            }
        }

        return mapper.map(savedSubject, SubjectUpsertResponse.class);
    }


    public void removeSubjectById(Long subId) {
        subjectRepository.findById(subId)
                .orElseThrow(() -> new NotFoundExceptionClass("subject with id: " + subId + " not found"));
        subjectRepository.deleteById(subId);
    }

    public Object upsertExamOfClass(ExamRequest examRequest) {

        ClassRoom classRoom = classroomRepository.findByIdAndUserId(examRequest.getClassId(), authService.getCurrentUser().getId());
        if (classRoom == null)
            throw new NotFoundExceptionClass("class with id: " + examRequest.getClassId() + " not found in user id: " + authService.getCurrentUser().getUsername());

        examValidation(examRequest);

        Exam exam = new Exam();
        exam.setExamDate(examRequest.getExamDate());
        exam.setClassRoom(classRoom);
        exam.setExamType(examRequest.getExamType());
        exam.setTitle(examRequest.getTitle());

        if (examRequest.getId() != null) { //when update old one
            examRepository.findById(examRequest.getId()).orElseThrow(() -> new NotFoundExceptionClass("exam with id: " + examRequest.getId() + " not found"));
            exam.setId(examRequest.getId());
            examRepository.save(exam);
        } else { //when create new
            Exam exam1 = examRepository.save(exam);
            autoFillScoreDefaultInit(classRoom, exam1);
        }
        return mapper.map(exam, ExamUpsertResponse.class);
    }

    private void examValidation(ExamRequest examRequest) {
        int month = examRequest.getExamDate().getMonthValue(); // Use getMonthValue() instead of getMonth() to get int
        int year = examRequest.getExamDate().getYear();
        ExamType examType = examRequest.getExamType();
        Long classId = examRequest.getClassId();

        Optional<Exam> existingExam = examRepository.findByMonthAndYearAndTypeAndClassId(month, year, examType, classId);

//        System.out.println("existingExam" + existingExam.toString());

        if (existingExam.isPresent()) {
            throw new NotFoundExceptionClass("Exam " + examType + " in " + month + "-" + year + " already exists");
        }
    }

    public List<ScoreUpsertResponse> upsertScoreOfExam(List<ScoreUpsertRequest> requests, Long examId, Long classId) {
        List<Score> scoresToSave = new ArrayList<>();

        // === Validate All ===
        for (ScoreUpsertRequest request : requests) {

            Student student = studentRepository.findByIdAndClassRoomId(request.getStudentId(), classId);
            if (student == null)
                throw new NotFoundExceptionClass("Student with id: " + request.getStudentId() + " not found");

            Subject subject = subjectRepository.findByIdAndClassRoomId(request.getSubjectId(), classId);
            if (subject == null) {
                throw new NotFoundExceptionClass("Subject with id: " + request.getSubjectId() +
                        " not found in classroom id: " + classId);
            }

            Score score = scoreRepository.findByStudentIdAndSubjectIdAndExamId(student.getId(), subject.getId(), examId);
            if(score == null) 
                throw new NotFoundExceptionClass("Score with id: " + student.getId() + " not found by Student id: " + student.getId()+ "and Subject id:" + subject.getId());

            Exam exam = examRepository.findByIdAndClassRoomId(examId, classId);
            if (exam == null) {
                throw new NotFoundExceptionClass("Exam with id: " + examId +
                        ", student id: " + student.getId() +
                        ", class id: " + classId + " not found");
            }

            // Prepare Score entity after validation
            score.setId(score.getId());
            score.setScore(request.getScore());

            scoresToSave.add(score);
        }

        // === Second Pass: Save All ===
        List<Score> savedScores = scoreRepository.saveAll(scoresToSave);

        return savedScores.stream()
                .map(score -> mapper.map(score, ScoreUpsertResponse.class))
                .collect(Collectors.toList());
    }



    private void autoFillScoreDefaultInit(ClassRoom classRoom, Exam exam) {

        List<Subject> subjects = subjectRepository.findByClassRoomId(classRoom.getId());

        if (subjects.isEmpty())
            throw new NotFoundExceptionClass("subject is empty in Classroom id: " + classRoom.getId());

        for (Student student : classRoom.getStudents()) {
            System.out.println("student >>>" + student.getFullName());
            for (Subject s : subjects) {
                Score score = new Score();
                score.setStudent(student);
                score.setExam(exam);
                score.setScore(0.0);
                score.setSubject(s);
                scoreRepository.save(score);
            }
        }
    }

    public void removeExamAndScoreById(Long examId) {
        examRepository.findById(examId)
                .orElseThrow(() -> new NotFoundExceptionClass("Exam with id: " + examId + " not found"));
        examRepository.deleteById(examId);
    }

    public List<ClassInfoResponse> getClassroomInfoByUserId() {
        Users user = authService.getCurrentUser();
        List<ClassRoom> classRooms = classroomRepository.findByUserId(user.getId());
        return classRooms.stream()
                .map(classRoom -> mapper.map(classRoom, ClassInfoResponse.class))
                .toList();
    }
}
