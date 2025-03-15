package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ballup_backend.entity.ConversationMemberEntity;

import jakarta.transaction.Transactional;

public interface ConversationMemberRepository  extends JpaRepository<ConversationMemberEntity, Long>{
    @Transactional
    @Modifying
    @Query("DELETE FROM ConversationMemberEntity cm WHERE cm.user.id = :userId AND cm.conversation.id = :conversationId")
    void deleteByUserIdAndConversationId(@Param("userId") Long userId, @Param("conversationId") Long conversationId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ConversationMemberEntity cm WHERE cm.conversation.id = :conversationId")
    void deleteByConversationId(@Param("conversationId") Long conversationId);
} 