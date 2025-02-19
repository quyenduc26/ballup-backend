package com.example.ballup_backend.dto.req.center;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateCenterRequest {
    
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(min = 20, max = 255)
    private String description;

    @NotBlank
    @Size(min = 8, max = 255)
    private String address;

    @NotNull 
    private Long ownerId;

    @NotEmpty 
    private List<String> images;
}
