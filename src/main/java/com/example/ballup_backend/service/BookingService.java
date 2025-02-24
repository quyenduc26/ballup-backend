package com.example.ballup_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.repository.BookingRepository;


@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public void confirmBookingRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != BookingEntity.BookingStatus.REQUESTED) {
            throw new RuntimeException("Booking is not in REQUESTED status");
        }
        booking.setStatus(BookingEntity.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    public void cancelBookingRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != BookingEntity.BookingStatus.REQUESTED) {
            throw new RuntimeException("Booking is not in REQUESTED status");
        }
        booking.setStatus(BookingEntity.BookingStatus.CANCEL);
        bookingRepository.save(booking);
    }

    public void rejectBookingRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != BookingEntity.BookingStatus.REQUESTED) {
            throw new RuntimeException("Booking is not in REQUESTED status");
        }
        booking.setStatus(BookingEntity.BookingStatus.REJECTED);
        bookingRepository.save(booking);
    }

    public void receivePaymentRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != BookingEntity.BookingStatus.DEPOSITED) {
            throw new RuntimeException("Booking is not in DEPOSITED status");
        }
        booking.setStatus(BookingEntity.BookingStatus.COMPLETED);
        bookingRepository.save(booking);
    }

    public void depositBookingRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != BookingEntity.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is not in CONFIRMED status");
        }
        booking.setStatus(BookingEntity.BookingStatus.DEPOSITED);
        bookingRepository.save(booking);
    }
}
