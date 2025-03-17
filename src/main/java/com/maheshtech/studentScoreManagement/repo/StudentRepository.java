package com.maheshtech.studentScoreManagement.repo;


import com.maheshtech.studentScoreManagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT s FROM Student s WHERE " +
            "(:studentNumber IS NULL OR LOWER(s.studentNumber) LIKE LOWER(CONCAT('%', :studentNumber, '%'))) AND " +
            "(:firstName IS NULL OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:emailAddress IS NULL OR LOWER(s.emailAddress) LIKE LOWER(CONCAT('%', :emailAddress, '%')))")
    List<Student> searchStudents(@Param("studentNumber") String studentNumber,
                                 @Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("emailAddress") String emailAddress);
}
