package com.example.ballup_backend.entity;import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "game-player")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private TeamEntity joinedTeam;

    @Enumerated(EnumType.STRING)
    @Column(name = "team", nullable = false)
    private GameTeam gameTeam;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    public enum GameTeam {
        TEAMA, TEAMB
    }
}
