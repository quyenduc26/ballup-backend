package com.example.ballup_backend.controller;


import com.example.ballup_backend.dto.req.payment.PaymentMethodRequest;
import com.example.ballup_backend.entity.PaymentMethodEntity;
import com.example.ballup_backend.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping
    public ResponseEntity<PaymentMethodEntity> createPaymentMethod(@RequestBody PaymentMethodRequest requestDto, @RequestParam Long ownerId) {
        PaymentMethodEntity paymentMethod = paymentMethodService.createPaymentMethod(requestDto, ownerId);
        return ResponseEntity.ok(paymentMethod);
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodEntity>> getAllPaymentMethods(@RequestParam Long ownerId) {
        List<PaymentMethodEntity> paymentMethods = paymentMethodService.getAllPaymentMethodsByOwner(ownerId);
        return ResponseEntity.ok(paymentMethods);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentMethodEntity> updatePaymentMethod( @PathVariable Long id, @RequestBody PaymentMethodRequest requestDto ) {
        PaymentMethodEntity updatedMethod = paymentMethodService.updatePaymentMethod(id, requestDto);
        return ResponseEntity.ok(updatedMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/set-active")
    public ResponseEntity<PaymentMethodEntity> setActivePaymentMethod(@PathVariable Long id) {
        PaymentMethodEntity updatedMethod = paymentMethodService.setActivePaymentMethod(id);
        return ResponseEntity.ok(updatedMethod);
    }

    @GetMapping("/active/by-booking")
    public ResponseEntity<PaymentMethodEntity> getActivePaymentMethodByBooking(@RequestParam Long bookingId) {
        PaymentMethodEntity activePaymentMethod = paymentMethodService.getActivePaymentMethodByBooking(bookingId);
        return ResponseEntity.ok(activePaymentMethod);
    }

}
