package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.slot.CreateSlotRequest;
import com.example.ballup_backend.service.PlayingSlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("owner/slot") 
public class PlayingSlotController {

    @Autowired
    private PlayingSlotService playingSlotService;

    @PostMapping
    public ResponseEntity<String> createSlot(@Valid @RequestBody CreateSlotRequest request) {
        playingSlotService.createPlayingSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Playing slot created successfully");
    }
}
