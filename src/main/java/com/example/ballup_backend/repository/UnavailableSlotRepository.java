package com.example.ballup_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;

@Repository
public interface UnavailableSlotRepository extends JpaRepository<UnavailableSlotEntity, Long> {
    @Query("SELECT u FROM UnavailableSlotEntity u JOIN FETCH u.creator WHERE u.slot = :slot")
    List<UnavailableSlotEntity> findBySlot(@Param("slot") PlayingSlotEntity slot);
}
