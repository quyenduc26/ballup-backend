package com.example.ballup_backend.dto.res.team;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamMemberResponse {
    private Long id;
    private String name;
    private String username;
    private String avatar;
    private Integer height;
    private Integer weight;
}   
