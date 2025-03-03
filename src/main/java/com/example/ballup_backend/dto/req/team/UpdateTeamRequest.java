package com.example.ballup_backend.dto.req.team;
import lombok.*;

@Getter
@Setter
@Builder
public class UpdateTeamRequest {
    private String name;
    private String address;
    private String intro;
    private String logo;
    private String cover;
    private String sport;
}