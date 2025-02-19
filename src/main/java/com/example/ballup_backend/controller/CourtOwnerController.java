package com.example.ballup_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
public class CourtOwnerController {
       
    @GetMapping
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Hello owner check");
    }
}
