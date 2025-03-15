package com.example.ballup_backend.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.res.slot.UnavailableSlotResponse;
import com.example.ballup_backend.entity.UnavailableSlotEntity;
import com.example.ballup_backend.repository.UnavailableSlotRepository;

@Service
public class UnavailableSlotService {
    @Autowired
    private UnavailableSlotRepository unavailableSlotRepository; 

    public boolean isSlotUnavailable(Long slotId, Timestamp fromTime, Timestamp toTime) {
        return unavailableSlotRepository.isSlotUnavailable(slotId, fromTime, toTime);
    }

    public List<UnavailableSlotResponse> getUnavailableSlotsByDate(Long slotId, long startOfDay, long endOfDay) {
        Timestamp startTimestamp = new Timestamp(startOfDay);
        Timestamp endTimestamp = new Timestamp(endOfDay);
    
        List<UnavailableSlotEntity> unavailableSlots = unavailableSlotRepository.findBySlotIdAndTimeRange(slotId, startTimestamp, endTimestamp);
    
        return unavailableSlots.stream()
        .map(slot -> {
            return UnavailableSlotResponse.builder()
                .id(slot.getId())
                .fromTime(slot.getFromTime())
                .toTime(slot.getToTime())
                .createBy(slot.getCreateBy())
                .creatorName(slot.getCreator().getUsername()) 
                .build();
        })
        .collect(Collectors.toList());
    }
    

    
}
