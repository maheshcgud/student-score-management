package com.maheshtech.studentScoreManagement.service;

import com.maheshtech.studentScoreManagement.entity.ScoreHistory;
import com.maheshtech.studentScoreManagement.entity.Student;
import com.maheshtech.studentScoreManagement.repo.ScoreHistoryRepository;
import com.maheshtech.studentScoreManagement.repo.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceImplTest {

    @Mock
    private ScoreHistoryRepository scoreHistoryRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    ScoreService scoreService = new ScoreServiceImpl();


    private Student student;
    private ScoreHistory scoreHistory;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("Mahesh");
        student.setLastName("Ch");

        scoreHistory = new ScoreHistory();
        scoreHistory.setStudent(student);
        scoreHistory.setScore(85.0);
        scoreHistory.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testAddStudentScore_Success() {
        // Mock student exists
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(scoreHistoryRepository.save(any(ScoreHistory.class))).thenReturn(scoreHistory);

        // Call service method
        ScoreHistory result = scoreService.addStudentScore(1L, 85.0);

        // Verify interactions
        verify(studentRepository, times(1)).findById(1L);
        verify(scoreHistoryRepository, times(1)).save(any(ScoreHistory.class));

        // Assert result
        assertNotNull(result);
        assertEquals(85.0, result.getScore());
        assertEquals("Mahesh", result.getStudent().getFirstName());
    }

    @Test
    void testAddStudentScore_StudentNotFound() {
        // Mock student not found
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            scoreService.addStudentScore(1L, 85.0);
        });

        assertEquals("Student not found with student number: 1", exception.getMessage());

        // Verify interactions
        verify(studentRepository, times(1)).findById(1L);
        verify(scoreHistoryRepository, never()).save(any(ScoreHistory.class));
    }
}