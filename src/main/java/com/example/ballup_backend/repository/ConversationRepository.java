package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ballup_backend.entity.ConversationEntity;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    
}
