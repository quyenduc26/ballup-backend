package com.example.ballup_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.owner.BookingRequestResponse;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.PlayingCenterRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;


@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

     @Autowired
    private PlayingCenterRepository playingCenterRepository; 

    @Autowired
    private PlayingCenterService playingCenterService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnavailableSlotRepository unavailableSlotRepository; 

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
    
    public List<BookingRequestResponse> getAllBookingRequests(Long ownerId) {
        UserEntity owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        
        List<Long> centerIds = playingCenterRepository.findCenterIdsByOwner(owner);
        
        List<Long> slotIds = new ArrayList<>();
        
        for (Long centerId : centerIds) {
            for (PlayingSlotEntity slot : playingCenterService.getPlayingSlotByCenterId(centerId)) {
                slotIds.add(slot.getId()); 
            }
        }
        List<Long> unavailableSlotIds = unavailableSlotRepository.findBySlotIdAndCreatedByUser(slotIds);
        List<BookingEntity> requestedBookings = bookingRepository.findRequestedBookingsByUnavailableSlots(BookingStatus.REQUESTED, unavailableSlotIds);
        List<BookingRequestResponse> requestBookingResponse = requestedBookings.stream().map(booking -> 
            BookingRequestResponse.builder()
                .id(booking.getId())
                .slotId(booking.getBookingSlot().getId())
                .creator(booking.getBookingSlot().getCreator().getUsername()) 
                .centerName(booking.getBookingSlot().getSlot().getPlayingCenter().getName())
                .fromTime(booking.getBookingSlot().getFromTime())
                .toTime(booking.getBookingSlot().getToTime())
                .createdAt(booking.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
        

        
        return requestBookingResponse;
    }

    
}
