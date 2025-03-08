package com.example.ballup_backend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.PlayingCenterEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("SELECT b FROM BookingEntity b WHERE b.bookingSlot.id IN :bookingSlots AND b.status = :requested")
    List<BookingEntity> findBySlotIdInAndStatus(@Param("bookingSlots") List<Long> bookingSlots, @Param("requested") BookingEntity.BookingStatus requested);

    @Query("SELECT b FROM BookingEntity b WHERE b.status = :status AND b.bookingSlot.id IN :unavailableSlotIds")
    List<BookingEntity> findBookingsByUnavailableSlotsAndStatus(
        @Param("status") BookingStatus status, 
        @Param("unavailableSlotIds") List<Long> unavailableSlotIds
    );

    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE b.bookingSlot.slot.playingCenter = :center")
    Long countByPlayingCenter(PlayingCenterEntity center);
}