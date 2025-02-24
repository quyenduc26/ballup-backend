package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;

    @PatchMapping("/deposit")
    public ResponseEntity<String> rejectBooking(@PathVariable Long bookingId) {
        bookingService.depositBookingRequest(bookingId);
        return ResponseEntity.ok("Booking deposited successfully");
    }

    @PatchMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBookingRequest(bookingId);
        return ResponseEntity.ok("Booking canceled successfully");
    }
}
