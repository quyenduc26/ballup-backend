package com.example.ballup_backend.dto.res.game;

import com.example.ballup_backend.dto.res.team.GameTeamResponse;
import com.example.ballup_backend.entity.TeamEntity.SportType;

import io.micrometer.common.lang.Nullable;
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
public class GameResponse {
    private Long id;
    private String name;
    private Timestamp fromTime;
    private Timestamp toTime;
    private String cover;
    private SportType type;
    private Long conversationId;
    private Long slotId;

    @Nullable
    private String centerName;
    private String slotName;
    private GameTeamResponse teamA;
    
    @Nullable
    private GameTeamResponse teamB;
    
}
