package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    public enum BookingStatus {
        REQUESTED, CANCEL, CONFIRMED, REJECTED, DEPOSITED, COMPLETED
    }

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true, nullable = true)
    private PaymentEntity payment;

    @OneToOne
    @JoinColumn(name = "unavailable_slot_id", unique = true, nullable = true)
    private UnavailableSlotEntity bookingSlot;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
