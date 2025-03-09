package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.res.booking.BookingDetailResponse;
import com.example.ballup_backend.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;

    @PatchMapping("/{bookingId}/deposit/{amount}")
    public ResponseEntity<String> depositBooking(@PathVariable Long bookingId, @PathVariable Long amount,  @RequestParam Long userId) {
        bookingService.depositBookingRequest(bookingId, amount, userId);
        return ResponseEntity.ok("Booking deposited successfully");
    }

    @PatchMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBookingRequest(bookingId);
        return ResponseEntity.ok("Booking canceled successfully");
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDetailResponse> getBookingDetail(@PathVariable Long bookingId) {
        BookingDetailResponse bookingDetail = bookingService.getBookingDetail(bookingId);
        return ResponseEntity.ok(bookingDetail);
    }
}
