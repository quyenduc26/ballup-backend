package com.example.ballup_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment-method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "bank_name", nullable = false, length = 150)
    private String bankName;

    @Column(name = "account_number", nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Column(name = "account_holder_name", nullable = false, length = 100)
    private String accountHolderName;

    @Column(name = "bank_branch", nullable = true,  length = 150)
    private String bankBranch;

    @Column(name = "qr_image_url", nullable = false, length = 255)
    private String qrImageUrl;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;
}
