package com.example.ballup_backend.dto.req.center;

import java.util.List;

import com.example.ballup_backend.entity.PlayingCenterEntity.PlayingCenterType;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateCenterRequest {
    @Size(max = 100)
    private String name;

    @Size(min = 20, max = 255)
    private String description;

    @Size(min = 8, max = 255)
    private String address;

    @NotEmpty 
    private List<String> images;

    @NotEmpty 
    private PlayingCenterType type;

    private PlayingCenterType centerType;
}
