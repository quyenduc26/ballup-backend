package com.example.ballup_backend.dto.res.team;

import com.example.ballup_backend.entity.TeamEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamResponse {
    private Long id;
    private String name;
    private String address;
    private String intro;
    private String logo;
    private String cover;
    private TeamEntity.SportType sport;
    private Long totalMembers;
}
