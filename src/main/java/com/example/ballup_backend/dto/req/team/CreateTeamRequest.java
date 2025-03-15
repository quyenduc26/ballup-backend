package com.example.ballup_backend.dto.req.team;


import com.example.ballup_backend.entity.TeamEntity.SportType;

import lombok.*;

@Getter
@Setter
@Builder
public class CreateTeamRequest {
    private String name;
    private String address;
    private String intro;
    private String logo;
    private String cover;
    private SportType sport;
    private Long userId;
}

