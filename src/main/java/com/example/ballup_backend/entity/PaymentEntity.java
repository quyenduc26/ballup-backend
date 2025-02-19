package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private UserEntity creator;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    public enum PaymentStatus {
        PENDING, SUCCESS, FAIL
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
