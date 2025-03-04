package com.example.ballup_backend.dto.res.center;

import java.util.List;

import com.example.ballup_backend.dto.res.slot.PlayingSlotResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayingCenterResponse {
    Long id;
    String name;
    String description;
    String address;
    List<String> imageUrls; 
    List<PlayingSlotResponse> slots; 
}
 