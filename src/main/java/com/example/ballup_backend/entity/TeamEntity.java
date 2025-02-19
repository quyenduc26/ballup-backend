package com.example.ballup_backend.entity;



import java.security.Timestamp;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long teamId;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "address", length = 256)
    private String address;

    @Column(name = "intro", length = 256)
    private String intro;

    @Column(name = "avatar", length = 256)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private ConversationEntity conversation;

    public enum Sport {
        FOOTBALL, BADMINTON
    }
    
}
