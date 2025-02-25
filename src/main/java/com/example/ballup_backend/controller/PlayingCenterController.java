package com.example.ballup_backend.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.res.center.CardPlayingCenterResponse;
import com.example.ballup_backend.dto.res.center.PlayingCenterResponse;
import com.example.ballup_backend.dto.res.slot.PlayingSlotResponse;
import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.service.PlayingCenterService;


@RestController
@RequestMapping("center")
public class PlayingCenterController {

    @Autowired
    PlayingCenterService playingCenterService;

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


    
    @GetMapping
    public ResponseEntity<Page<CardPlayingCenterResponse>> searchPlayingCenters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Long fromTime,
            @RequestParam(required = false) Long toTime,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        LocalDateTime fromDateTime = fromTime != null ? 
            LocalDateTime.ofInstant(Instant.ofEpochMilli(fromTime), ZoneId.systemDefault()) : null;
    
        LocalDateTime toDateTime = toTime != null ? 
            LocalDateTime.ofInstant(Instant.ofEpochMilli(toTime), ZoneId.systemDefault()) : null;
    
        Page<CardPlayingCenterResponse> result = playingCenterService.getCenterByCriteria(name, address, fromDateTime, toDateTime, sortBy, sortDirection, page, size);
        return ResponseEntity.ok(result);
    }
    
    
}
