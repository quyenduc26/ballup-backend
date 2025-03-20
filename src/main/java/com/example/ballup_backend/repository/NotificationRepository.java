package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ballup_backend.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    
}
