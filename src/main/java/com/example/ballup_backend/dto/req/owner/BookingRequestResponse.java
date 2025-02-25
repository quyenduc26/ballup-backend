package com.example.ballup_backend.dto.req.owner;


import java.sql.Timestamp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestResponse {
    Long id;
    Long slotId;
    String centerName;
    String creator;
    Long amount;
    Timestamp fromTime;
    Timestamp toTime;
    Timestamp createdAt;
}



