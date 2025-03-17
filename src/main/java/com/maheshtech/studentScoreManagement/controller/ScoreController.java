package com.maheshtech.studentScoreManagement.controller;


import com.maheshtech.studentScoreManagement.entity.ScoreHistory;
import com.maheshtech.studentScoreManagement.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/scores")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping("/{studentId}")
    public ResponseEntity<?> addScore(@PathVariable Long studentId, @RequestParam Double score) {
        try {
            ScoreHistory savedScore = scoreService.addStudentScore(studentId, score);
            return ResponseEntity.ok("Score added successfully for student " + studentId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
