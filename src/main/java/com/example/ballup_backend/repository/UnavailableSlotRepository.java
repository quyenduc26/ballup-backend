package com.example.ballup_backend.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;

@Repository
public interface UnavailableSlotRepository extends JpaRepository<UnavailableSlotEntity, Long> {
    @Query("SELECT u FROM UnavailableSlotEntity u JOIN FETCH u.creator WHERE u.slot = :slot AND u.status IN ('PROCESSING', 'DONE')")
    List<UnavailableSlotEntity> findBySlot(@Param("slot") PlayingSlotEntity slot);

    @Query("SELECT u.slot.id FROM UnavailableSlotEntity u " +
        "WHERE ((u.fromTime BETWEEN :fromDateTime AND :toDateTime) " +
        "   OR (u.toTime BETWEEN :fromDateTime AND :toDateTime) " +
        "   OR (:fromDateTime BETWEEN u.fromTime AND u.toTime) " +
        "   OR (:toDateTime BETWEEN u.fromTime AND u.toTime)) " +
        "AND u.status IN ('PROCESSING', 'DONE')")
    List<Long> findUnavailableSlots(@Param("fromDateTime") Timestamp fromDateTime, @Param("toDateTime") Timestamp toDateTime);



    @Query("SELECT u.id FROM UnavailableSlotEntity u WHERE u.slot.id IN :slotIds AND u.createBy = 'BY_USER'")
    List<Long> findBySlotIdAndCreatedByUser(@Param("slotIds") List<Long> slotIds);

    @Query("""
    SELECT COUNT(u) > 0 FROM UnavailableSlotEntity u
    WHERE u.slot.id = :slotId 
    AND (
        (u.fromTime BETWEEN :fromTime AND :toTime) 
        OR (u.toTime BETWEEN :fromTime AND :toTime)
        OR (:fromTime BETWEEN u.fromTime AND u.toTime) 
        OR (:toTime BETWEEN u.fromTime AND u.toTime)
        )
    AND u.status IN ('PROCESSING', 'DONE')
    """)
    boolean isSlotUnavailable(@Param("slotId") Long slotId, @Param("fromTime") Timestamp fromTime, @Param("toTime") Timestamp toTime);


    @Query("SELECT us FROM UnavailableSlotEntity us WHERE us.slot.id = :slotId AND us.fromTime >= :startOfDay AND us.toTime <= :endOfDay AND us.status IN ('PROCESSING', 'DONE')")
    List<UnavailableSlotEntity> findBySlotIdAndTimeRange(
        @Param("slotId") Long slotId,
        @Param("startOfDay") Timestamp startOfDay,
        @Param("endOfDay") Timestamp endOfDay
    );


    @Query("""
        SELECT u FROM UnavailableSlotEntity u 
        WHERE u.slot.id = :slotId 
        AND u.fromTime > CURRENT_TIMESTAMP 
        AND u.createBy  = 'BY_USER' 
        AND u.status IN ('PROCESSING', 'DONE')
        ORDER BY u.fromTime DESC
        """)
    List<UnavailableSlotEntity> findUpcomingUnavailableSlots(@Param("slotId") Long slotId);
    
    @Query("""
        SELECT u FROM UnavailableSlotEntity u 
        WHERE u.slot.id IN :slotIds 
        AND u.fromTime > CURRENT_TIMESTAMP 
        AND u.createBy = 'BY_USER' 
        AND u.status IN ('PROCESSING', 'DONE')
        ORDER BY u.slot.id, u.fromTime DESC
        """)
    List<UnavailableSlotEntity> findUpcomingUnavailableSlotsBySlotIds(@Param("slotIds") List<Long> slotIds);
    


}
