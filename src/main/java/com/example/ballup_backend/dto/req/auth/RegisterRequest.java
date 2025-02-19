package com.example.ballup_backend.dto.req.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank @Size(max = 50)
    private String username;

    @NotBlank @Email @Size(max = 100)
    private String email;

    @NotBlank @Size(min = 8, max = 255)
    private String password;

    @NotBlank 
    private String role;
}
