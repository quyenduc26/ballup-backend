package com.example.ballup_backend.dto.req.team;

import com.example.ballup_backend.entity.TeamMemberEntity.Role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRoleRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long teamId;

    @NotNull
    private Role newRole;
}
