package com.example.ballup_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.center.CreateCenterRequest;
import com.example.ballup_backend.dto.res.center.PlayingCenterResponse;
import com.example.ballup_backend.dto.res.slot.PlayingSlotResponse;
import com.example.ballup_backend.service.PlayingCenterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("owner/center")
public class PlayingCenterController {

    @Autowired
    PlayingCenterService playingCenterService;

    @PostMapping
    public ResponseEntity<String> createCenter(@Valid @RequestBody CreateCenterRequest request) {
        playingCenterService.createPlayingCenter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Center created successfully!");
    }

    @GetMapping("/{id}")
    public  ResponseEntity<PlayingCenterResponse> getPlayingCenter(@PathVariable Long id) {
        PlayingCenterResponse playingCenterResponse = playingCenterService.getCenterInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(playingCenterResponse);
    }

    @GetMapping("/{centerId}/slot")
    public ResponseEntity<List<PlayingSlotResponse>> getSlotsByCenter(@PathVariable Long centerId) {
        List<PlayingSlotResponse> slots = playingCenterService.getPlayingSlotByCenterId(centerId)
         .stream()
        .map(slot -> PlayingSlotResponse.builder()
            .id(slot.getId())
            .name(slot.getName())
            .primaryPrice(slot.getPrimaryPrice())
            .nightPrice(slot.getNightPrice())
            .build())
        .collect(Collectors.toList());
        return ResponseEntity.ok(slots);
    }
    
    
}
