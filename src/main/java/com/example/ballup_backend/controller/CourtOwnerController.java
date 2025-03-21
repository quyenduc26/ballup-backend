package com.example.ballup_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.center.CreateCenterRequest;
import com.example.ballup_backend.dto.req.center.UpdateCenterRequest;
import com.example.ballup_backend.dto.req.owner.BookingRequestResponse;
import com.example.ballup_backend.dto.req.owner.PaymentRequestResponse;
import com.example.ballup_backend.dto.req.slot.CreateSlotRequest;
import com.example.ballup_backend.dto.req.slot.DisableSlotRequest;
import com.example.ballup_backend.dto.req.slot.UpdateSlotRequest;
import com.example.ballup_backend.dto.res.center.PlayingCenterResponse;
import com.example.ballup_backend.service.BookingService;
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

    @Autowired
    private BookingService bookingService;
       
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

    @GetMapping("/{ownerId}/centers")
    public ResponseEntity<List<PlayingCenterResponse>> getCentersByOwner(@PathVariable Long ownerId) {
        List<PlayingCenterResponse> centers = playingCenterService.getCentersByOwnerId(ownerId);
        return ResponseEntity.ok(centers);
    }

    @PatchMapping("/booking/{bookingId}/confirm")
    public ResponseEntity<String> confirmBooking(@PathVariable Long bookingId) {
        bookingService.confirmBookingRequest(bookingId);
        return ResponseEntity.ok("Booking confirmed successfully");
    }

    @PatchMapping("/booking/{bookingId}/receive")
    public ResponseEntity<String> receivePayment(@PathVariable Long bookingId) {
        bookingService.receivePaymentRequest(bookingId);
        return ResponseEntity.ok("Booking payment received successfully");
    }

    @PatchMapping("/booking/{bookingId}/reject")
    public ResponseEntity<String> rejectBooking(@PathVariable Long bookingId) {
        bookingService.rejectBookingRequest(bookingId);
        return ResponseEntity.ok("Booking deposited successfully");
    }

    //get request 
   @GetMapping("booking/requests/{ownerId}")
    public ResponseEntity<List<BookingRequestResponse>> getRequestBooking(@PathVariable Long ownerId) {
        List<BookingRequestResponse> bookingRequests = bookingService.getAllBookingRequests(ownerId);
        return ResponseEntity.ok(bookingRequests);
    }

    @GetMapping("booking/payments/{ownerId}")
    public ResponseEntity<List<PaymentRequestResponse>> getRequestPayments(@PathVariable Long ownerId) {
        List<PaymentRequestResponse> bookingRequests = bookingService.getAllPaymentRequests(ownerId);
        return ResponseEntity.ok(bookingRequests);
    }

    //update 
    @PatchMapping("center/update/{id}")
    public ResponseEntity<String> updatePlayingCenter(@PathVariable Long id, @RequestBody UpdateCenterRequest request) {
        playingCenterService.updatePlayingCenter(id, request);
        return ResponseEntity.ok("Updated center successfully");
    }

    @PatchMapping("slot/update/{id}")
    public ResponseEntity<String> updatePlayingSlot( @PathVariable Long id, @Valid @RequestBody UpdateSlotRequest request) {
        playingSlotService.updatePlayingSlot(id, request);
        return ResponseEntity.ok("Updated playing slot successfully");
    }

    //delete
    @DeleteMapping("center/delete/{id}")
    public ResponseEntity<String> deletePlayingCenter(@PathVariable Long id) {
        playingCenterService.deletePlayingCenter(id);
        return ResponseEntity.ok("Deleted center successfully");
    }

    @DeleteMapping("slot/delete/{id}")
    public ResponseEntity<String> deletePlayingSlot(@PathVariable Long id) {
        playingSlotService.deletePlayingSlot(id);
        return ResponseEntity.ok("Deleted playing slot successfully");
    }

    



}
