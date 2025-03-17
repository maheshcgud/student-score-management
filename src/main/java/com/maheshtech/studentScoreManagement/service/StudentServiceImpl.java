package com.maheshtech.studentScoreManagement.service;


import com.maheshtech.studentScoreManagement.dto.StudentRequest;
import com.maheshtech.studentScoreManagement.dto.StudentResponse;
import com.maheshtech.studentScoreManagement.entity.ScoreHistory;
import com.maheshtech.studentScoreManagement.entity.Student;
import com.maheshtech.studentScoreManagement.exception.StudentFoundException;
import com.maheshtech.studentScoreManagement.repo.ScoreHistoryRepository;
import com.maheshtech.studentScoreManagement.repo.StudentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScoreHistoryRepository scoreHistoryRepository;

    @Override
    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return getStudentResponses(students);
    }

    @Transactional
    @Override
    public StudentResponse registerStudent(StudentRequest studentRequest) {
        Optional<Student> existingStudent = studentRepository.findByFirstNameAndLastName(
                studentRequest.getFirstName(), studentRequest.getLastName()
        );

        if (existingStudent.isPresent()) {
            throw new StudentFoundException("A student with the same first and last name already exists.");
        }

        Student student = new Student();
        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setDateOfBirth(studentRequest.getDateOfBirth());
        student.setEmailAddress(studentRequest.getEmailAddress());
        student.setCellphoneNumber(studentRequest.getCellphoneNumber());

        Student savedStudent = studentRepository.save(student);

        // save score in ScoreHistory DB
        if (studentRequest.getCurrentScore() != null) {
            ScoreHistory scoreHistory = new ScoreHistory();
            scoreHistory.setStudent(savedStudent);
            scoreHistory.setScore(studentRequest.getCurrentScore());
            scoreHistory.setTimestamp(LocalDateTime.now());

            ScoreHistory savedScoreHistory = scoreHistoryRepository.save(scoreHistory);
            savedStudent.setScoreHistory(List.of(savedScoreHistory));
        }
        return convertToResponseDTO(savedStudent);
    }

    @Transactional
    @Override
    public Student updateStudent(Long studentId, Student updatedStudent) {
        // Fetch the existing student by student number
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with student number: " + studentId));

        // Check if the updated first name and last name already exist in another student
        Optional<Student> studentWithSameName = studentRepository.findByFirstNameAndLastName(
                updatedStudent.getFirstName(), updatedStudent.getLastName());

        if (studentWithSameName.isPresent() && !studentWithSameName.get().getId().equals(studentId)) {
            throw new RuntimeException("A student with the same first and last name already exists.");
        }

        // Update student details
        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
        existingStudent.setCellphoneNumber(updatedStudent.getCellphoneNumber());
        existingStudent.setEmailAddress(updatedStudent.getEmailAddress());
        existingStudent.generateStudentNumber();

        // Save updated student
        return studentRepository.save(existingStudent);
    }

    @Transactional
    @Override
    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found with student number: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public StudentResponse convertToResponseDTO(Student student) {
        List<ScoreHistory> scores = student.getScoreHistory();

        double currentScore = scores.isEmpty() ? 0.0 : scores.get(scores.size() - 1).getScore();
        double averageScore = scores.stream()
                .mapToDouble(ScoreHistory::getScore)
                .average()
                .orElse(0.0);

        return StudentResponse.builder()
                .id(student.getId())
                .studentNumber(student.getStudentNumber())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .cellphoneNumber(student.getCellphoneNumber())
                .emailAddress(student.getEmailAddress())
                .currentScore(currentScore)
                .averageScore(averageScore)
                .build();
    }

    @Override
    public List<StudentResponse> searchStudents(String studentNumber, String firstName, String lastName, String email) {
        List<Student> students = studentRepository.searchStudents(studentNumber, firstName, lastName, email);
        return getStudentResponses(students);
    }

    private List<StudentResponse> getStudentResponses(List<Student> students) {
        List<ScoreHistory> allScores = scoreHistoryRepository.findAll();

        // Group scores by studentId
        Map<Long, List<ScoreHistory>> scoresByStudentId = allScores.stream()
                .collect(Collectors.groupingBy(score -> score.getStudent().getId()));

        return students.stream().map(student -> {
            List<ScoreHistory> scores = scoresByStudentId.getOrDefault(student.getId(), List.of());

            // Calculate Average Score
            double averageScore = scores.stream()
                    .mapToDouble(ScoreHistory::getScore)
                    .average()
                    .orElse(0.0); // Default 0 if no scores available

            // Find Current Score
            double currentScore = scores.stream()
                    .max(Comparator.comparing(ScoreHistory::getTimestamp)) // Get the latest score by timestamp
                    .map(ScoreHistory::getScore)
                    .orElse(0.0); // Default 0 if no scores

            return StudentResponse.builder()
                    .id(student.getId())
                    .studentNumber(student.getStudentNumber())
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .dateOfBirth(student.getDateOfBirth())
                    .cellphoneNumber(student.getCellphoneNumber())
                    .emailAddress(student.getEmailAddress())
                    .currentScore(currentScore)
                    .averageScore(averageScore)
                    .build();
        }).collect(Collectors.toList());
    }
}
