package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "Payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "booking_field_id", nullable = false, unique = true)
    private BookingFieldEntity bookingField;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "created", nullable = false)
    @Builder.Default
    private Instant created = Instant.now();
}
