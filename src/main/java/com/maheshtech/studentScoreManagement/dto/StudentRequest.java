package com.maheshtech.studentScoreManagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRequest {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String emailAddress;
    private String cellphoneNumber;
    private Double currentScore; // Optional Score
}
