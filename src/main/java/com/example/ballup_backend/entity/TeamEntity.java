package com.example.ballup_backend.entity;



import java.security.Timestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "create_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createAt;

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
