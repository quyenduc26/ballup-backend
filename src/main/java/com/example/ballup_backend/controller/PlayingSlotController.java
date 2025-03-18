package com.example.ballup_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.slot.DisableSlotRequest;
import com.example.ballup_backend.dto.res.slot.UnavailableSlotResponse;
import com.example.ballup_backend.service.PlayingSlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("slot") 
public class PlayingSlotController {

    @Autowired
    private PlayingSlotService playingSlotService;


    @PostMapping("/takeSlot")
    public ResponseEntity<Long> disableSlot(@Valid @RequestBody DisableSlotRequest request) {
        Long id = playingSlotService.disableSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

   @GetMapping("/{slotId}/disabled")
    public ResponseEntity<List<UnavailableSlotResponse>> getUnavailableSlots(@PathVariable Long slotId) {
        List<UnavailableSlotResponse> unavailableSlots = playingSlotService.getDisabledSlots(slotId);
        return ResponseEntity.ok(unavailableSlots);
    }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<String> deletePlayingSlot(@PathVariable Long slotId) {
        playingSlotService.deletePlayingSlot(slotId);
        return ResponseEntity.ok("Playing slot deleted successfully!");
    }


    
}
