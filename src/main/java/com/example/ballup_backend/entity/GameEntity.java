package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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

    @Column(name = "name", nullable = false)
    private String name; 

    @Column(name = "fromTime", nullable = false)
    private Timestamp fromTime; 

    @Column(name = "toTime", nullable = false)
    private Timestamp toTime; 

    @Column(name = "address", nullable = false)
    private String address; 

    @Column(name = "description", nullable = false)
    private String description; 

    @Column(name = "cover", nullable = false)
    private String cover; 

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private UserEntity creator;

    @OneToOne
    @JoinColumn(name = "conversation_id", unique = true, nullable = true)
    private ConversationEntity conversation;

    @ManyToOne
    @JoinColumn(name = "playing_slot_id", nullable = true)
    private PlayingSlotEntity playingSlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GameType type;

    public enum GameType {
        FOOTBALL,BADMINTON
    }
}

