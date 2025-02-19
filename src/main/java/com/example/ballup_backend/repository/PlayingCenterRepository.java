package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.PlayingCenterEntity;

@Repository
public interface PlayingCenterRepository extends JpaRepository<PlayingCenterEntity, Long>{

    
}
