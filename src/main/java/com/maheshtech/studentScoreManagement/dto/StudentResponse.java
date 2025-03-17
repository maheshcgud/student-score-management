package com.maheshtech.studentScoreManagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long id;
    private String studentNumber;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String cellphoneNumber;
    private String emailAddress;
    private double currentScore;  // Latest Score
    private double averageScore;
    private String studentFullName;

    public String getStudentFullName() {
        return this.firstName + " " + this.lastName;
    }
}
