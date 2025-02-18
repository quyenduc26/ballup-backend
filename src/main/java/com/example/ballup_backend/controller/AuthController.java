package com.example.ballup_backend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.authRequest.LoginRequest;
import com.example.ballup_backend.dto.req.authRequest.RegisterRequest;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.service.AuthService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

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

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException, java.io.IOException {
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/auth"
                + "?client_id=716334625485-no0t2ct5s9a3g55lnn7tja6ggsq3b7tk.apps.googleusercontent.com"
                + "&redirect_uri=http://localhost:8080/auth/google/callback"
                + "&response_type=code"
                + "&scope=email%20profile";
        response.sendRedirect(googleAuthUrl);
    }   


    
}
