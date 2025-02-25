package com.example.ballup_backend.dto.res.slot;

import java.sql.Timestamp;

import com.example.ballup_backend.entity.UnavailableSlotEntity.createdBy;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnavailableSlotResponse {
    Long id;
    Timestamp fromTime;
    Timestamp toTime;
    createdBy createBy;
    String creatorName;
}
