package com.example.ballup_backend.dto.res.booking;

import java.sql.Timestamp;

import com.example.ballup_backend.entity.BookingEntity.BookingStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingDetailResponse {
    private Long bookingId;
    private String user;
    private String centerName;
    private String centerAddress;
    private String slotName;
    private Long amount;
    private BookingStatus status;
    private Timestamp bookingTime;
}
