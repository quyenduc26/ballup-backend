package com.example.ballup_backend.dto.req.slot;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateSlotRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private Integer primaryPrice;

    private Integer nightPrice;


}




