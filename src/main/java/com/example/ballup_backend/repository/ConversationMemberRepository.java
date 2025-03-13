package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ballup_backend.entity.ConversationMemberEntity;

public interface ConversationMemberRepository  extends JpaRepository<ConversationMemberEntity, Long>{

    
} 