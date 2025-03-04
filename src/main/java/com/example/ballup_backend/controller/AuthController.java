package com.example.ballup_backend.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.auth.LoginRequest;
import com.example.ballup_backend.dto.req.auth.RegisterRequest;
import com.example.ballup_backend.dto.res.user.LoginResponse;
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
        authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
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

    @GetMapping("/test/current-user")
    public ResponseEntity<?> getCurrentUserRole(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities() != null) {
            return ResponseEntity.ok(
                Map.of(
                    "username", authentication.getName(),
                    "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                )
            );
        }
        return ResponseEntity.ok(Map.of("message", "No authentication found"));
    }


    
}
