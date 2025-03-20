package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity forUser;

    @Column(name = "is_read")
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private TeamEntity team;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = true)
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = true)
    private BookingEntity booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType  type;

    public enum NotificationType {
        BOOKING_REQUESTED, 
        BOOKING_DEPOSITED, 
        BOOKING_CANCELLED, 
        BOOKING_CONFIRMED, 
        BOOKING_REJECTED, 
        BOOKING_LATE_PAID, 
        BOOKING_SUCCEEDED,
        TEAM_JOINED, 
        TEAM_LEFT, 
        TEAM_KICKED, 
        TEAM_DELETED,
        GAME_JOINED, 
        GAME_OPPONENT_JOINED, 
        GAME_LEFT, 
        GAME_DELETED
    }
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}



