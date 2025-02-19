package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time", nullable = false)
    private Timestamp time; 

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private UserEntity creator;

    @OneToOne
    @JoinColumn(name = "conversation_id", unique = true)
    private ConversationEntity conversation;

    @ManyToOne
    @JoinColumn(name = "playing_slot_id", nullable = false)
    private PlayingSlotEntity field;
}

