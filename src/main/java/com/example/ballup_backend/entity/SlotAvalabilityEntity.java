package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "slot_availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotAvalabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "return_time", nullable = false)
    private Timestamp returnTime;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private UserEntity creator;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    public enum BookingStatus {
        REQUESTED, CONFIRMED, REJECTED, DEPOSITED, COMPLETED
    }

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private PaymentEntity payment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
