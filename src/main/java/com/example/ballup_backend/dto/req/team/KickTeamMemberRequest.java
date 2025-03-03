package com.example.ballup_backend.dto.req.team;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KickTeamMemberRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long teamId;

}
