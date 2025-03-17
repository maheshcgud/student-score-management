package com.maheshtech.studentScoreManagement.service;

import com.maheshtech.studentScoreManagement.dto.StudentRequest;
import com.maheshtech.studentScoreManagement.dto.StudentResponse;
import com.maheshtech.studentScoreManagement.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    public List<StudentResponse> getAllStudents();

    public StudentResponse registerStudent(StudentRequest studentRequest);

    public Student updateStudent(Long studentId, Student updatedStudent);

    public void deleteStudent(Long studentId);

    public StudentResponse convertToResponseDTO(Student student);

    public Optional<Student> findById(Long studentId);

    public List<StudentResponse> searchStudents(String studentNumber, String firstName, String lastName, String email);
}
