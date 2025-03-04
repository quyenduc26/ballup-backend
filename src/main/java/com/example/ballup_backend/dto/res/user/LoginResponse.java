package com.example.ballup_backend.dto.res.user;

import com.example.ballup_backend.entity.UserEntity.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String avatar;
    String username;
    Long id;
    Role role;
    String token;
}
