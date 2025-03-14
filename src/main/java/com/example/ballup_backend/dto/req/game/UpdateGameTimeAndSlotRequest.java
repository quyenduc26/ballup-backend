package com.example.ballup_backend.dto.req.game;

import jakarta.annotation.Nullable;
import lombok.*;

@Setter
@Getter
@Builder
@Data
public class UpdateGameTimeAndSlotRequest {
    private Long gameId;

    @Nullable
    private Long fromTime;

    @Nullable
    private Long toTime;

    @Nullable
    private Long newSlotId; 
}