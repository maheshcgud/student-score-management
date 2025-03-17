package com.maheshtech.studentScoreManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"first_name", "last_name"})
})
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_number", unique = true, nullable = false)
    private String studentNumber; // calculated in system

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only alphabetic characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only alphabetic characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank
    @Pattern(regexp = "^[+]?[0-9]+$", message = "Cellphone number can only contain numeric characters and an optional leading plus sign")
    @Column(name = "cellphone_number", nullable = false)
    private String cellphoneNumber;

    @NotBlank
    @Email(message = "Invalid email format")
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @JsonManagedReference
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ScoreHistory> scoreHistory = new ArrayList<>();

    @PrePersist
    public void generateStudentNumber() {
        if (this.firstName != null && this.lastName != null) {
            String firstInitial = this.firstName.substring(0, 1).toUpperCase();
            String lastInitial = this.lastName.substring(0, 1).toUpperCase();
            String uniqueNumber = String.format("%06d", (int) (Math.random() * 1000000));
            this.studentNumber = firstInitial + uniqueNumber + lastInitial;
        }
    }

    /*public @NotBlank @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only alphabetic characters") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only alphabetic characters") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only alphabetic characters") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only alphabetic characters") String lastName) {
        this.lastName = lastName;
    }*/

    public @NotNull String getDateOfBirth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dateOfBirth.format(formatter);
    }

    public void setDateOfBirth(@NotNull String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateOfBirth = LocalDate.parse(dateOfBirth, formatter);
    }

    /*public @NotBlank @Pattern(regexp = "^[+]?[0-9]+$", message = "Cellphone number can only contain numeric characters and an optional leading plus sign") String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(@NotBlank @Pattern(regexp = "^[+]?[0-9]+$", message = "Cellphone number can only contain numeric characters and an optional leading plus sign") String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }*/

    /*public @NotBlank @Email(message = "Invalid email format") String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(@NotBlank @Email(message = "Invalid email format") String emailAddress) {
        this.emailAddress = emailAddress;
    }*/

    /*public double calculateAverageScore() {
        if (scoreHistory != null && !scoreHistory.isEmpty()) {
            return scoreHistory.stream()
                    .map(ScoreHistory::getScore)
                    .collect(Collectors.averagingDouble(Double::doubleValue));
        } else {
            return 0.0;
        }
    }*/
}

