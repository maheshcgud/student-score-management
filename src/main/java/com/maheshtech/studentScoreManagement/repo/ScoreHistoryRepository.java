package com.maheshtech.studentScoreManagement.repo;

import com.maheshtech.studentScoreManagement.entity.ScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, Long> {
    List<ScoreHistory> findByStudentId(Long studentId);
}
