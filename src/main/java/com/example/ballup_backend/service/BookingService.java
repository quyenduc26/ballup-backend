package com.example.ballup_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.owner.BookingRequestResponse;
import com.example.ballup_backend.dto.req.owner.PaymentRequestResponse;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.PaymentEntity;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.PaymentEntity.PaymentStatus;
import com.example.ballup_backend.entity.UnavailableSlotEntity.Status;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.PaymentRepository;
import com.example.ballup_backend.repository.PlayingCenterRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;

import jakarta.transaction.Transactional;


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

    @Autowired
    private PaymentRepository paymentRepository; 

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

        PaymentEntity payment = paymentRepository.getReferenceById(booking.getPayment().getId());
        payment.setStatus(PaymentStatus.SUCCESS);

        UnavailableSlotEntity unavailableSlotEntity = unavailableSlotRepository.getReferenceById(booking.getBookingSlot().getId());
        unavailableSlotEntity.setStatus(Status.DONE);

        bookingRepository.save(booking);
        paymentRepository.save(payment);
        unavailableSlotRepository.save(unavailableSlotEntity);
    }

    @Transactional
    public void depositBookingRequest(Long bookingId, Long amount, Long userId) {

        //check valid
        BookingEntity booking = bookingRepository.getReferenceById(bookingId);
        UserEntity user = userRepository.getReferenceById(userId);
        if (booking.getStatus() != BookingEntity.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is not in CONFIRMED status");
        }

        //update status cho unavailable slot
        UnavailableSlotEntity unavailableSlot = unavailableSlotRepository.getReferenceById(booking.getBookingSlot().getId());
        unavailableSlot.setStatus(Status.PROCESSING);
        unavailableSlotRepository.save(unavailableSlot);

        //tạo payment cho booking
        PaymentEntity bookingPayment = PaymentEntity.builder()
            .amount(amount)
            .creator(user)
            .status(PaymentStatus.PENDING)
            .build();
        PaymentEntity savedPaymentEntity = paymentRepository.save(bookingPayment);

        //save 
        booking.setStatus(BookingEntity.BookingStatus.DEPOSITED);
        booking.setPayment(savedPaymentEntity);
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
        List<BookingEntity> requestedBookings = bookingRepository.findBookingsByUnavailableSlotsAndStatus(BookingStatus.REQUESTED, unavailableSlotIds);
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

    public List<PaymentRequestResponse> getAllPaymentRequests(Long ownerId) {
        //check owner
        UserEntity owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        //get owner's slots
        List<Long> centerIds = playingCenterRepository.findCenterIdsByOwner(owner);
        List<Long> slotIds = new ArrayList<>();
        for (Long centerId : centerIds) {
            for (PlayingSlotEntity slot : playingCenterService.getPlayingSlotByCenterId(centerId)) {
                slotIds.add(slot.getId()); 
            }
        }

        //get deposited bookings
        List<Long> unavailableSlotIds = unavailableSlotRepository.findBySlotIdAndCreatedByUser(slotIds);
        List<BookingEntity> depositedBookings = bookingRepository.findBookingsByUnavailableSlotsAndStatus(BookingStatus.DEPOSITED, unavailableSlotIds);

        List<PaymentRequestResponse> payementBookingResponse = depositedBookings.stream().map(booking -> 
            PaymentRequestResponse.builder()
                .id(booking.getId())
                .creator(booking.getBookingSlot().getCreator().getUsername())
                .amount(booking.getPayment().getAmount())
                .bookingId(booking.getId())
                .createdAt(booking.getPayment().getCreatedAt())
                .build()
        ).collect(Collectors.toList());
        return payementBookingResponse;
    }

    
}
