package com.example.ballup_backend.dto.res.center;

import com.example.ballup_backend.entity.PlayingCenterEntity.PlayingCenterType;

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
    PlayingCenterType type; 
    Long bookingCount; 
    String image;
    Integer primaryPrice;
    Integer nightPrice;
}
