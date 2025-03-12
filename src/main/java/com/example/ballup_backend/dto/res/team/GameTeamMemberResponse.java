package com.example.ballup_backend.dto.res.team;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameTeamMemberResponse {
    private String avatar;
    private String firstName;
    private String lastName;
    
}
