package com.example.ballup_backend.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.res.slot.UnavailableSlotResponse;
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

    @GetMapping("/{slotId}/blocked")
    public List<UnavailableSlotResponse> getSlotUnavailable(@PathVariable Long slotId, @RequestParam Long startOfDay, @RequestParam Long endOfDay) {
        return unavailableSlotService.getUnavailableSlotsByDate(slotId, startOfDay, endOfDay);
    }

    @GetMapping("/slot/{slotId}")
    public ResponseEntity<List<UnavailableSlotResponse>> getUnavailableSlotsBySlotId(@PathVariable Long slotId) {
        List<UnavailableSlotResponse> responses = unavailableSlotService.getUnavailableSlotsById(slotId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<UnavailableSlotResponse>> getUnavailableSlotsByCenterId(@PathVariable Long centerId) {
        List<UnavailableSlotResponse> responses = unavailableSlotService.getUnavailableSlotsByCenterId(centerId);
        return ResponseEntity.ok(responses);
    }
    
}
