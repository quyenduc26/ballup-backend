package com.example.ballup_backend.dto.req.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank  @Size(max = 100)
    private String emailOrUsername;

    @Size(min = 8, max = 255)
    private String password;
}
