package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.service.NotificationService;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestParam Long userId, @RequestParam Long ownerId ) {        
        notificationService.notifyUser(userId, "Your booking is confirmed!");
        notificationService.notifyOwner(ownerId, "A new booking has been made at your center.");
        
        return ResponseEntity.ok("Ngon luônnnn");
    }
}
