package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ballup_backend.entity.GameEntity;


public interface GameRepository extends JpaRepository<GameEntity, Long> {
    
}
