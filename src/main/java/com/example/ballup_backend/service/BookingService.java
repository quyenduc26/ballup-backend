package com.example.ballup_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.owner.BookingRequestResponse;
import com.example.ballup_backend.dto.req.owner.PaymentRequestResponse;
import com.example.ballup_backend.dto.res.booking.BookingDetailResponse;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.PaymentEntity;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.NotificationEntity.NotificationType;
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

import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public void confirmBookingRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        UserEntity user = userRepository.getReferenceById(booking.getPayment().getCreator().getId());
        if (booking.getStatus() != BookingEntity.BookingStatus.REQUESTED) {
            throw new RuntimeException("Booking is not in REQUESTED status");
        }
        booking.setStatus(BookingEntity.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        notificationService.createUserBookingNotification(user, booking, NotificationType.BOOKING_CONFIRMED );
    }

    public void cancelBookingRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        UserEntity user = booking.getBookingSlot().getSlot().getPlayingCenter().getOwner();
        if (booking.getStatus() != BookingEntity.BookingStatus.REQUESTED && booking.getStatus() != BookingEntity.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is not in REQUESTED or CONFIRMED status");
        }   
        booking.setStatus(BookingEntity.BookingStatus.CANCEL);
        bookingRepository.save(booking);
        notificationService.createOwnerBookingNotification(user, booking, NotificationType.BOOKING_CANCELLED );

    }

    public void rejectBookingRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        UserEntity user = booking.getPayment().getCreator();
        if (booking.getStatus() != BookingEntity.BookingStatus.REQUESTED && booking.getStatus() != BookingEntity.BookingStatus.DEPOSITED) {
            throw new RuntimeException("Booking is not in REQUESTED status");
        }
        booking.setStatus(BookingEntity.BookingStatus.REJECTED);
        bookingRepository.save(booking);
        notificationService.createUserBookingNotification(user, booking, NotificationType.BOOKING_REJECTED );

    }

    public void receivePaymentRequest(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != BookingEntity.BookingStatus.DEPOSITED) {
            throw new RuntimeException("Booking is not in DEPOSITED status");
        }
        UserEntity user = booking.getPayment().getCreator();
        booking.setStatus(BookingEntity.BookingStatus.COMPLETED);

        PaymentEntity payment = paymentRepository.getReferenceById(booking.getPayment().getId());
        payment.setStatus(PaymentStatus.SUCCESS);

        UnavailableSlotEntity unavailableSlotEntity = unavailableSlotRepository.getReferenceById(booking.getBookingSlot().getId());
        unavailableSlotEntity.setStatus(Status.DONE);

        bookingRepository.save(booking);
        paymentRepository.save(payment);
        unavailableSlotRepository.save(unavailableSlotEntity);
        notificationService.createUserBookingNotification(user, booking, NotificationType.BOOKING_SUCCEEDED );


    }

    @Transactional
    public void depositBookingRequest(Long bookingId) {

        //check valid
        BookingEntity booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getStatus() != BookingEntity.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is not in CONFIRMED status");
        }
        UserEntity user = booking.getBookingSlot().getSlot().getPlayingCenter().getOwner();
        //update status cho unavailable slot
        UnavailableSlotEntity unavailableSlot = unavailableSlotRepository.getReferenceById(booking.getBookingSlot().getId());
        unavailableSlot.setStatus(Status.PROCESSING);
        unavailableSlotRepository.save(unavailableSlot); 
        
        //save 
        booking.setStatus(BookingEntity.BookingStatus.DEPOSITED);
        bookingRepository.save(booking);
        notificationService.createOwnerBookingNotification(user, booking, NotificationType.BOOKING_DEPOSITED );
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
                .createdAt(booking.getPayment().getCreatedAt())
                .build()
        ).collect(Collectors.toList());
        return payementBookingResponse;
    }

    public BookingDetailResponse getBookingDetail(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .map(booking -> BookingDetailResponse.builder()
                        .bookingId(booking.getId())
                        .user(booking.getPayment().getCreator().getUsername())
                        .amount(booking.getPayment().getAmount())
                        .slotName(booking.getBookingSlot().getSlot().getName())
                        .centerName(booking.getBookingSlot().getSlot().getPlayingCenter().getName())
                        .centerAddress(booking.getBookingSlot().getSlot().getPlayingCenter().getAddress())
                        .bookingTime(booking.getCreatedAt())
                        .fromTime(booking.getBookingSlot().getFromTime())
                        .toTime(booking.getBookingSlot().getToTime())
                        .status(booking.getStatus())
                        .build()
                )
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
    }
    
    public List<BookingDetailResponse> getAllBookingsByUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        List<BookingEntity> userBookings = bookingRepository.findBookingsByUserId(userId);
    
        return userBookings.stream()
            .map(booking -> BookingDetailResponse.builder()
                    .bookingId(booking.getId())
                    .user(booking.getPayment().getCreator().getUsername())
                    .amount(booking.getPayment().getAmount())
                    .slotName(booking.getBookingSlot().getSlot().getName())
                    .centerName(booking.getBookingSlot().getSlot().getPlayingCenter().getName())
                    .centerAddress(booking.getBookingSlot().getSlot().getPlayingCenter().getAddress())
                    .bookingTime(booking.getCreatedAt())
                    .fromTime(booking.getBookingSlot().getFromTime())
                    .toTime(booking.getBookingSlot().getToTime())
                    .status(booking.getStatus())
                    .build()
            ).collect(Collectors.toList());
    }
    
}
