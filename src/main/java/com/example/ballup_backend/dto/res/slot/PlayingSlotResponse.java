package com.example.ballup_backend.dto.res.slot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayingSlotResponse {
    Long id;
    String name;
    Integer primaryPrice;
    Integer nightPrice;
}
