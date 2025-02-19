package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationEntity conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @Column(name = "content", length = 256, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MessageType type;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    public enum MessageType {
        TEXT, IMAGE, VIDEO
    }
}



