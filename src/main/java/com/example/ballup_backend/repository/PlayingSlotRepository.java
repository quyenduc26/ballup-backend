package com.example.ballup_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;

@Repository
public interface PlayingSlotRepository extends JpaRepository<PlayingSlotEntity, Long> {
     List<PlayingSlotEntity> findByPlayingCenter(PlayingCenterEntity playingCenter);

     @Query("SELECT ps.playingCenter.id, ps.id FROM PlayingSlotEntity ps WHERE ps.id IN :slotIds")
     List<Object[]> findCentersBySlotIds(@Param("slotIds") List<Long> slotIds);

     @Query("SELECT s.id FROM PlayingSlotEntity s WHERE s.playingCenter = :playingCenter")
     List<Long> findSlotIdsByPlayingCenter(PlayingCenterEntity playingCenter);

     
    @Query("SELECT ps FROM PlayingSlotEntity ps WHERE ps.playingCenter = :center")
    List<PlayingSlotEntity> findSlotsByCenter(@Param("center") PlayingCenterEntity center);


}