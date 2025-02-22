package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.center.CreateCenterRequest;
import com.example.ballup_backend.dto.req.slot.CreateSlotRequest;
import com.example.ballup_backend.dto.req.slot.DisableSlotRequest;
import com.example.ballup_backend.service.PlayingCenterService;
import com.example.ballup_backend.service.PlayingSlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/owner")
public class CourtOwnerController {

    @Autowired
    private PlayingSlotService playingSlotService;

    @Autowired
    PlayingCenterService playingCenterService;
       
    @GetMapping
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Hello owner check");
    }

    @PostMapping("/center")
    public ResponseEntity<String> createCenter(@Valid @RequestBody CreateCenterRequest request) {
        playingCenterService.createPlayingCenter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Center created successfully!");
    }

    @PostMapping("/slot")
    public ResponseEntity<String> createSlot(@Valid @RequestBody CreateSlotRequest request) {
        playingSlotService.createPlayingSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Playing slot created successfully");
    }

    @PostMapping("slot/disable")
    public ResponseEntity<String> disableSlot(@Valid @RequestBody DisableSlotRequest request) {
        playingSlotService.disableSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Playing slot has been disabled successfully.");
    }
}
