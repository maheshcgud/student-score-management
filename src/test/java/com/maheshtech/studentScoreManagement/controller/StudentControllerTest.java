package com.maheshtech.studentScoreManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maheshtech.studentScoreManagement.dto.StudentRequest;
import com.maheshtech.studentScoreManagement.dto.StudentResponse;
import com.maheshtech.studentScoreManagement.entity.Student;
import com.maheshtech.studentScoreManagement.exception.GlobalExceptionHandler;
import com.maheshtech.studentScoreManagement.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student student;
    private StudentRequest studentRequest;
    private StudentResponse studentResponse;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        student = new Student();
        student.setId(1L);
        student.setFirstName("Mahesh");
        student.setLastName("Ch");
        student.setDateOfBirth("01/01/1985");
        student.setEmailAddress("mahesh.ch@test.com");
        student.setCellphoneNumber("+123456789");

        studentRequest = new StudentRequest();
        studentRequest.setFirstName("Mahesh");
        studentRequest.setLastName("Ch");
        studentRequest.setDateOfBirth("01/01/1985");
        studentRequest.setEmailAddress("mahesh.ch@test.com");
        studentRequest.setCellphoneNumber("+123456789");

        studentResponse = new StudentResponse();
        studentResponse.setId(1L);
        studentResponse.setFirstName("Mahesh");
        studentResponse.setLastName("Ch");
        studentResponse.setDateOfBirth("01/01/1985");
        studentResponse.setEmailAddress("mahesh.ch@test.com");
        studentResponse.setCellphoneNumber("+123456789");
    }

    @Test
    void testRegisterStudent_Success() throws Exception {
        when(studentService.registerStudent(any(StudentRequest.class))).thenReturn(studentResponse);

        mockMvc.perform(post("/students/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mahesh"));

        verify(studentService, times(1)).registerStudent(any(StudentRequest.class));
    }

    @Test
    void testGetAllStudents_Success() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(studentResponse));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Mahesh"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testUpdateStudent_Success() throws Exception {
        when(studentService.updateStudent(anyLong(), any(Student.class))).thenReturn(student);
        when(studentService.convertToResponseDTO(any(Student.class))).thenReturn(studentResponse);

        mockMvc.perform(put("/students/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mahesh"));

        verify(studentService, times(1)).updateStudent(anyLong(), any(Student.class));
    }

    @Test
    void testUpdateStudent_StudentNotFound() throws Exception {
        when(studentService.updateStudent(anyLong(), any(Student.class)))
                .thenThrow(new RuntimeException("Student not found"));

        mockMvc.perform(put("/students/{studentId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Student not found"));

        verify(studentService, times(1)).updateStudent(anyLong(), any(Student.class));
    }

    @Test
    void testDeleteStudent_Success() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/{studentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Student deleted successfully"));

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void testDeleteStudent_StudentNotFound() throws Exception {
        doThrow(new RuntimeException("Student not found")).when(studentService).deleteStudent(2L);

        mockMvc.perform(delete("/students/{studentId}", 2L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Student not found"));

        verify(studentService, times(1)).deleteStudent(2L);
    }

    @Test
    void testGetStudentById_Success() throws Exception {
        when(studentService.findById(1L)).thenReturn(Optional.of(student));
        when(studentService.convertToResponseDTO(student)).thenReturn(studentResponse);

        mockMvc.perform(get("/students/{studentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mahesh"));

        verify(studentService, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        when(studentService.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/{studentId}", 2L))
                .andExpect(status().isNotFound())  // Ensure 404 is returned
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND")) // Check JSON key
                .andExpect(jsonPath("$.message").value("Student not found")); // Check message

        verify(studentService, times(1)).findById(2L);
    }

    @Test
    void testSearchStudents_Success() throws Exception {
        when(studentService.searchStudents(any(), any(), any(), any()))
                .thenReturn(List.of(studentResponse));

        mockMvc.perform(get("/students/search")
                        .param("firstName", "Mahesh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Mahesh"));

        verify(studentService, times(1)).searchStudents(any(), any(), any(), any());
    }

}