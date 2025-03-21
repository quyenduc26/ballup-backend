package com.example.ballup_backend.dto.res.team;

import java.util.List;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameTeamResponse {
    private String name;
    private String intro;
    private String logo;
    private List<GameTeamMemberResponse> members;

}
