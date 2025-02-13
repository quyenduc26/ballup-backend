package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "Booking_Field")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_field_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private FieldEntity field;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "return_time", nullable = false)
    private Instant returnTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    public enum BookingStatus {
        REQUESTED, CONFIRMED, REJECTED, DEPOSITED, COMPLETED
    }

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
