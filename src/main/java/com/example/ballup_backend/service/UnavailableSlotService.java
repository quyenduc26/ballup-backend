package com.example.ballup_backend.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.repository.UnavailableSlotRepository;

@Service
public class UnavailableSlotService {
    @Autowired
    private UnavailableSlotRepository unavailableSlotRepository; 

    public boolean isSlotUnavailable(Long slotId, Timestamp fromTime, Timestamp toTime) {
        return unavailableSlotRepository.isSlotUnavailable(slotId, fromTime, toTime);
    }
}
