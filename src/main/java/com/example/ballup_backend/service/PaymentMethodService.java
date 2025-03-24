package com.example.ballup_backend.service;

import com.example.ballup_backend.dto.req.payment.PaymentMethodRequest;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.PaymentMethodEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.PaymentMethodRepository;
import com.example.ballup_backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public PaymentMethodEntity createPaymentMethod(PaymentMethodRequest request, Long ownerId) {
        UserEntity owner = userRepository.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Owner not found"));
    
        PaymentMethodEntity paymentMethod = PaymentMethodEntity.builder()
            .name(request.getName())
            .bankName(request.getBankName())
            .accountNumber(request.getAccountNumber())
            .accountHolderName(request.getAccountHolderName())
            .bankBranch(request.getBankBranch())
            .qrImageUrl(request.getQrImageUrl())
            .instructions(request.getInstructions())
            .isActive(request.getIsActive())
            .owner(owner)
            .build();
    
        return paymentMethodRepository.save(paymentMethod);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentMethodEntity> getAllPaymentMethodsByOwner(Long ownerId) {
        UserEntity owner = userRepository.findById(ownerId)
            .orElseThrow(() -> new EntityNotFoundException("Owner not found with ID: " + ownerId));
        
        return paymentMethodRepository.findByOwner(owner);
    }

    @Transactional
    public PaymentMethodEntity updatePaymentMethod(Long id, PaymentMethodRequest request) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found for this owner"));

        if (request.getName() != null) {
            paymentMethod.setName(request.getName());
        }
        if (request.getBankName() != null) {
            paymentMethod.setBankName(request.getBankName());
        }
        if (request.getAccountNumber() != null) {
            paymentMethod.setAccountNumber(request.getAccountNumber());
        }
        if (request.getAccountHolderName() != null) {
            paymentMethod.setAccountHolderName(request.getAccountHolderName());
        }
        if (request.getBankBranch() != null) {
            paymentMethod.setBankBranch(request.getBankBranch());
        }
        if (request.getQrImageUrl() != null) {
            paymentMethod.setQrImageUrl(request.getQrImageUrl());
        }
        if (request.getInstructions() != null) {
            paymentMethod.setInstructions(request.getInstructions());
        }
        if (request.getIsActive() != null) {
            paymentMethod.setActive(request.getIsActive());
        }

        return paymentMethodRepository.save(paymentMethod);
    }


    @Transactional
    public void deletePaymentMethod(Long id) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found for this owner"));

        paymentMethodRepository.delete(paymentMethod);
    }

    @Transactional
    public PaymentMethodEntity setActivePaymentMethod(Long id) {
        PaymentMethodEntity methodToActivate = paymentMethodRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment method not found"));

        // Tắt tất cả các phương thức thanh toán của chủ sở hữu
        paymentMethodRepository.deactivateAllByOwner(methodToActivate.getOwner().getId());

        // Đặt phương thức được chọn là active
        methodToActivate.setActive(true);
        
        return paymentMethodRepository.save(methodToActivate);
    }

    @Transactional(readOnly = true)
    public PaymentMethodEntity getActivePaymentMethodByBooking(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        UserEntity owner = booking.getBookingSlot().getSlot().getPlayingCenter().getOwner();

        return paymentMethodRepository.findActivePaymentMethodByOwnerId(owner.getId())
            .orElseThrow(() -> new EntityNotFoundException("No active payment method found for owner"));
    }

}
