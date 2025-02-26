package com.example.ballup_backend.dto.req.team;


import com.example.ballup_backend.entity.TeamEntity.Sport;

import lombok.*;

@Getter
@Setter
public class CreateTeamRequest {
    private String name;
    private String address;
    private String intro;
    private String logo;
    private String cover;
    private Sport sport;
    private Long userId;
}

