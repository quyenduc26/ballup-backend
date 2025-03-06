package com.example.ballup_backend.dto.req.game;

import java.sql.Timestamp;
import java.util.List;

import com.example.ballup_backend.entity.GameEntity.GameType;

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
    String name;
    Long fromTime;
    Long toTime;
    String location;
    String description;
    String cover;
    List<Long> memberIdList;
    GameType type;
    @Builder.Default Long slotId = null;
}
