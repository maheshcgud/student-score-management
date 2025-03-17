package com.maheshtech.studentScoreManagement.service;

import com.maheshtech.studentScoreManagement.dto.StudentRequest;
import com.maheshtech.studentScoreManagement.dto.StudentResponse;
import com.maheshtech.studentScoreManagement.entity.ScoreHistory;
import com.maheshtech.studentScoreManagement.entity.Student;
import com.maheshtech.studentScoreManagement.exception.StudentFoundException;
import com.maheshtech.studentScoreManagement.repo.ScoreHistoryRepository;
import com.maheshtech.studentScoreManagement.repo.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ScoreHistoryRepository scoreHistoryRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentRequest studentRequest;
    private ScoreHistory scoreHistory;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("Mahesh");
        student.setLastName("Ch");
        student.setDateOfBirth("01/01/1985");
        student.setEmailAddress("mahesh.ch@test.com");
        student.setCellphoneNumber("+123456789");

        studentRequest = new StudentRequest();
        studentRequest.setFirstName("Mahesh");
        studentRequest.setLastName("Ch");
        studentRequest.setDateOfBirth("01/01/1985");
        studentRequest.setEmailAddress("mahesh.ch@test.com");
        studentRequest.setCellphoneNumber("+123456789");
        studentRequest.setCurrentScore(90.0);

        scoreHistory = new ScoreHistory();
        scoreHistory.setStudent(student);
        scoreHistory.setScore(90.0);
        scoreHistory.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        when(scoreHistoryRepository.findAll()).thenReturn(List.of(scoreHistory));

        List<StudentResponse> students = studentService.getAllStudents();

        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals("Mahesh", students.get(0).getFirstName());
    }

    @Test
    void testRegisterStudent_Success() {
        when(studentRepository.findByFirstNameAndLastName("Mahesh", "Ch")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(scoreHistoryRepository.save(any(ScoreHistory.class))).thenReturn(scoreHistory);

        StudentResponse response = studentService.registerStudent(studentRequest);

        assertNotNull(response);
        assertEquals("Mahesh", response.getFirstName());
        assertEquals(90.0, response.getCurrentScore());

        verify(studentRepository, times(1)).save(any(Student.class));
        verify(scoreHistoryRepository, times(1)).save(any(ScoreHistory.class));
    }

    @Test
    void testRegisterStudent_ThrowsExceptionIfStudentExists() {
        when(studentRepository.findByFirstNameAndLastName("Mahesh", "Ch")).thenReturn(Optional.of(student));

        StudentFoundException exception = assertThrows(StudentFoundException.class, () -> {
            studentService.registerStudent(studentRequest);
        });

        assertEquals("A student with the same first and last name already exists.", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.findByFirstNameAndLastName("Mahesh", "Ch")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student updatedStudent = new Student();
        updatedStudent.setFirstName("Mahesh");
        updatedStudent.setLastName("Ch");
        updatedStudent.setDateOfBirth("01/01/1985");
        updatedStudent.setEmailAddress("mahesh.ch@test.com");
        updatedStudent.setCellphoneNumber("+123456789");

        Student result = studentService.updateStudent(1L, updatedStudent);

        assertNotNull(result);
        assertEquals("Mahesh", result.getFirstName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_ThrowsExceptionIfNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(1L, student);
        });

        assertEquals("Student not found with student number: 1", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testDeleteStudent_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudent_ThrowsExceptionIfNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.deleteStudent(1L);
        });

        assertEquals("Student not found with student number: 1", exception.getMessage());
        verify(studentRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> foundStudent = studentService.findById(1L);

        assertTrue(foundStudent.isPresent());
        assertEquals("Mahesh", foundStudent.get().getFirstName());
    }

    @Test
    void testSearchStudents() {
        when(studentRepository.searchStudents("M12345C", "Mahesh", "Ch", "mahesh.ch@test.com"))
                .thenReturn(List.of(student));
        when(scoreHistoryRepository.findAll()).thenReturn(List.of(scoreHistory));

        List<StudentResponse> results = studentService.searchStudents("M12345C", "Mahesh", "Ch", "mahesh.ch@test.com");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Mahesh", results.get(0).getFirstName());
    }
}