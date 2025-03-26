package com.example.ballup_backend.dto.res.booking;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class CompletedBookingResponse {
    private Long id;
    private Long slotId;
    private String creator;
    private String centerName;
    private Timestamp fromTime;
    private Timestamp toTime;
    private Long amount;
    private Timestamp createdAt;
}
