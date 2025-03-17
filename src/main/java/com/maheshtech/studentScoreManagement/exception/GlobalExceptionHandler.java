package com.maheshtech.studentScoreManagement.exception;


import com.maheshtech.studentScoreManagement.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(StudentFoundException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ErrorMessage employeeNotFoundException(StudentFoundException employeeNotFoundException){
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.FOUND,
                employeeNotFoundException.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage employeeNotFoundException(Exception exception){
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleStudentNotFoundException(StudentNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());
        return errorMessage;
    }
}
