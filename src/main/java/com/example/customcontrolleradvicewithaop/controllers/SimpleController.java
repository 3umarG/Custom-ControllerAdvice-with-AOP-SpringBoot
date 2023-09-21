package com.example.customcontrolleradvicewithaop.controllers;

import com.example.customcontrolleradvicewithaop.annotations.ControllerHandler;
import com.example.customcontrolleradvicewithaop.exceptions.CustomBadRequestException;
import com.example.customcontrolleradvicewithaop.exceptions.CustomNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@ControllerHandler
public class SimpleController {

    @GetMapping("/not-found")
    public ResponseEntity<?> getNotFoundTest(){
        throw new CustomNotFoundException("Not Found Resources");
    }

    @GetMapping("/bad-request")
    public ResponseEntity<?> getBadRequestTest(){
        throw new CustomBadRequestException("Bad Request");
    }


    /**
     * That the easy part, we actually do that also when using ControllerAdvice for Spring Boot
     */
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(CustomBadRequestException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(CustomNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

}

