package com.maheshtech.studentScoreManagement.controller;

import com.maheshtech.studentScoreManagement.entity.ScoreHistory;
import com.maheshtech.studentScoreManagement.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ScoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private ScoreController scoreController;

    private ScoreHistory scoreHistory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scoreController).build();

        scoreHistory = new ScoreHistory();
        scoreHistory.setId(1L);
        scoreHistory.setScore(95.0);
    }

    @Test
    void testAddScore_Success() throws Exception {
        Long studentId = 1L;
        Double score = 95.0;

        when(scoreService.addStudentScore(studentId, score)).thenReturn(scoreHistory);

        mockMvc.perform(post("/scores/{studentId}", studentId)
                        .param("score", score.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Score added successfully for student 1"));

        verify(scoreService, times(1)).addStudentScore(studentId, score);
    }

    @Test
    void testAddScore_StudentNotFound() throws Exception {
        Long studentId = 2L;
        Double score = 90.0;

        when(scoreService.addStudentScore(studentId, score))
                .thenThrow(new RuntimeException("Student not found"));

        mockMvc.perform(post("/scores/{studentId}", studentId)
                        .param("score", score.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Student not found"));

        verify(scoreService, times(1)).addStudentScore(studentId, score);
    }
}