package com.maheshtech.studentScoreManagement.service;

import com.maheshtech.studentScoreManagement.entity.ScoreHistory;

public interface ScoreService {

    public ScoreHistory addStudentScore(Long studentId, Double score);
}
