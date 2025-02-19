package com.example.ballup_backend.dto.res.center;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayingCenterResponse {
    String name;
    String description;
    String address;
    List<String> imageUrls;
}
