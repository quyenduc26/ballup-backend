package com.example.ballup_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;

@Repository
public interface PlayingSlotRepository extends JpaRepository<PlayingSlotEntity, Long> {
     List<PlayingSlotEntity> findByPlayingCenter(PlayingCenterEntity playingCenter);
}