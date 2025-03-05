package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ballup_backend.entity.GamePlayerEntity;

public interface GamePlayerRepository extends JpaRepository<GamePlayerEntity, Long> {

    
} 
