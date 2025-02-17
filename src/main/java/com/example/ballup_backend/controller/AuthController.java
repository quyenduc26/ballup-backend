package com.example.ballup_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.authRequest.LoginRequest;
import com.example.ballup_backend.dto.req.authRequest.RegisterRequest;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.service.AuthService;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

   @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        UserEntity user = authService.registerUser(request);
        URI location = URI.create("/users/" + user.getId());
        return ResponseEntity.created(location).body("User registered successfully!");
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }
    
}
