package com.example.ballup_backend.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.service.UnavailableSlotService;


@RestController
@RequestMapping("/unavailable-slot")
public class UnavailableSlotController {
    
    @Autowired
    private UnavailableSlotService unavailableSlotService;

    @GetMapping("/check/{slotId}")
    public boolean checkSlotUnavailable(@PathVariable Long slotId, @RequestParam Long fromTime, @RequestParam Long toTime) {
        Timestamp fromTimestamp = new Timestamp(fromTime);
        Timestamp toTimestamp = new Timestamp(toTime);
        
        return unavailableSlotService.isSlotUnavailable(slotId, fromTimestamp, toTimestamp);
    }
    
}
