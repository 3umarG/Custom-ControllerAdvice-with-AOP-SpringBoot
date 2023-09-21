package com.example.customcontrolleradvicewithaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CustomControllerAdviceWithAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomControllerAdviceWithAopApplication.class, args);
    }

}
