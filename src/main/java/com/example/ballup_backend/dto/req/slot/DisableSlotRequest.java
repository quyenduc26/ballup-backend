package com.example.ballup_backend.dto.req.slot;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DisableSlotRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long playingSlotId;

    @NotNull
    private Long fromTime;

    private Long toTime;

    @Nullable
    private Long amount;


}




