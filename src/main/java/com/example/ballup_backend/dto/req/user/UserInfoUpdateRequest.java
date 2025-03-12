package com.example.ballup_backend.dto.req.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String phone;
    private Integer weight;
    private Integer height;
}
