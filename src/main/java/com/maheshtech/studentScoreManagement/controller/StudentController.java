package com.maheshtech.studentScoreManagement.controller;


import com.maheshtech.studentScoreManagement.dto.StudentRequest;
import com.maheshtech.studentScoreManagement.dto.StudentResponse;
import com.maheshtech.studentScoreManagement.entity.Student;
import com.maheshtech.studentScoreManagement.exception.StudentNotFoundException;
import com.maheshtech.studentScoreManagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> registerStudent(@RequestBody StudentRequest student) {
        return ResponseEntity.ok(studentService.registerStudent(student));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId, @RequestBody Student student) {
        try {
            Student updatedStudent = studentService.updateStudent(studentId, student);
            StudentResponse response = studentService.convertToResponseDTO(updatedStudent);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        try {
            studentService.deleteStudent(studentId);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long studentId) {
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        StudentResponse response = studentService.convertToResponseDTO(student);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String emailAddress) {
        List<StudentResponse> students = studentService.searchStudents(studentNumber, firstName, lastName, emailAddress);
        return ResponseEntity.ok(students);
    }
}
