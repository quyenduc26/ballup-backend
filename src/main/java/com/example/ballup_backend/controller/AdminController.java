package com.example.ballup_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Hello admin check");
    }
    
}
