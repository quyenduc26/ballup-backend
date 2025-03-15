package com.example.ballup_backend.dto.res.game;

import com.example.ballup_backend.dto.res.team.TeamOverviewResponse;
import com.example.ballup_backend.entity.TeamEntity.SportType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MyGameResponse {
    private Long id;
    private String name;
    private Timestamp fromTime;
    private Timestamp toTime;
    private String center;
    private String cover;
    private SportType type;
    private Long conversationId;
    private Long slotId;
    private boolean isCreator;
    private TeamOverviewResponse teamA;
    private TeamOverviewResponse teamB;
    
}
