package com.example.customcontrolleradvicewithaop.controllers;

import com.example.customcontrolleradvicewithaop.annotations.ControllerHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@ControllerHandler
public class SimpleController {
}
