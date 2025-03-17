package com.example.ballup_backend.dto.req.game;

import java.util.List;

import com.example.ballup_backend.entity.TeamEntity.SportType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGameRequest {
    Long userId;
    Long userTeamId;
    String name;
    Long fromTime;
    Long toTime;
    String address;
    String description;
    String cover; 
    Integer membersRequired;
    List<Long> memberIdList;
    SportType type;
    @Builder.Default Long slotId = null;
}
