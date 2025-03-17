package com.maheshtech.studentScoreManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "score_history")
@Getter
@Setter
public class ScoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_number", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Student student;

    @NotNull
    @Min(value = 0, message = "Score must be positive")
    @Max(value = 100, message = "Score cannot be greater than 100")
    private Double score;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public @NotNull @Min(value = 0, message = "Score must be positive") Double getScore() {
        return score;
    }

    public void setScore(@NotNull @Min(value = 0, message = "Score must be positive") Double score) {
        this.score = score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }*/

}
