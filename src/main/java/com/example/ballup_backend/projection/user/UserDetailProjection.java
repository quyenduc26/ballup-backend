package com.example.ballup_backend.projection.user;


import com.example.ballup_backend.entity.UserEntity.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailProjection {
    String username;
    String email;
    Role role;
}
