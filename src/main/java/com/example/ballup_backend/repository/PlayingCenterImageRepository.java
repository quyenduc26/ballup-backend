package com.example.ballup_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingCenterImageEntity;

import jakarta.transaction.Transactional;

public interface PlayingCenterImageRepository extends JpaRepository<PlayingCenterImageEntity, Long>{

    @Query("SELECT p FROM PlayingCenterImageEntity p WHERE p.center = :center")
    List<PlayingCenterImageEntity> findByCenter(@Param("center") PlayingCenterEntity center);

    @Modifying
    @Transactional
    @Query("DELETE FROM PlayingCenterImageEntity p WHERE p.center = :playingCenter")
    void deleteByPlayingCenter(@Param("playingCenter") PlayingCenterEntity playingCenter);

}
