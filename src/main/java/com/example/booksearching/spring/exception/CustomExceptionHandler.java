package com.example.booksearching.spring.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> customExceptionHandler(CustomException e) {
        log.error("An exception occurred with custom message: {}", e.getCustomExceptionType().getErrorMsg(), e);
        return ResponseEntity.status(e.getCustomExceptionType().getHttpStatus())
                .body(e.getCustomExceptionType().getErrorMsg());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<String> constraintViolationExceptionHandler(Exception e) {
        log.error("Validation error occurred: {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
    }

}
