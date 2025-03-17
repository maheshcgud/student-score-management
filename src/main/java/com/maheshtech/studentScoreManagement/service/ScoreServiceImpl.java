package com.maheshtech.studentScoreManagement.service;


import com.maheshtech.studentScoreManagement.entity.ScoreHistory;
import com.maheshtech.studentScoreManagement.entity.Student;
import com.maheshtech.studentScoreManagement.repo.ScoreHistoryRepository;
import com.maheshtech.studentScoreManagement.repo.StudentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
public class ScoreServiceImpl implements ScoreService{

    @Autowired
    private ScoreHistoryRepository scoreHistoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ScoreHistory addStudentScore(Long studentId, Double score) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with student number: " + studentId));

        ScoreHistory scoreHistory = new ScoreHistory();
        scoreHistory.setStudent(student);
        scoreHistory.setScore(score);
        scoreHistory.setTimestamp(LocalDateTime.now());

        return scoreHistoryRepository.save(scoreHistory);
    }
}
