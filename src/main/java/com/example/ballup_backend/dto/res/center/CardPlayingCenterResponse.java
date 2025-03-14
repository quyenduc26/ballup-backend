package com.example.ballup_backend.dto.res.center;

import com.example.ballup_backend.entity.TeamEntity.SportType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardPlayingCenterResponse {
    Long id;
    String name;
    String address;
    SportType type; 
    Long bookingCount; 
    String image;
    Integer primaryPrice;
    Integer nightPrice;
}
