package com.example.ballup_backend.controller;

import java.util.List;

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

    @PatchMapping("/{bookingId}/deposit")
    public ResponseEntity<String> depositBooking(@PathVariable Long bookingId) {
        bookingService.depositBookingRequest(bookingId);
        return ResponseEntity.ok("Booking deposited successfully");
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBookingRequest(bookingId);
        return ResponseEntity.ok("Booking canceled successfully");
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDetailResponse> getBookingDetail(@PathVariable Long bookingId) {
        BookingDetailResponse bookingDetail = bookingService.getBookingDetail(bookingId);
        return ResponseEntity.ok(bookingDetail);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDetailResponse>> getAllBookingsByUser(@PathVariable Long userId) {
        List<BookingDetailResponse> bookings = bookingService.getAllBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }
}
