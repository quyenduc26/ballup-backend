package com.example.ballup_backend.dto.res.team;

import java.util.List;

import com.example.ballup_backend.entity.TeamEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamDetailResponse {
    private Long id;
    private String name;
    private String address;
    private String intro;
    private String logo;
    private String cover;
    private TeamEntity.Sport sport;
    private List<TeamMemberResponse> members;
}
