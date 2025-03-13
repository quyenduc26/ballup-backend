package com.example.ballup_backend.dto.req.game;
import lombok.*;

@Setter
@Getter
@Builder
public class UpdateGameInfoRequest {
    private Long gameId;
    private String name;
    private String cover;
    private String description;
    private Integer membersRequired;
}