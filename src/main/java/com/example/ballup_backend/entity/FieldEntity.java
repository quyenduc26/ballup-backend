package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "Field")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "primary_price", nullable = false)
    private Integer primaryPrice;

    @Column(name = "night_price", nullable = false)
    private Integer nightPrice;

    @ManyToOne
    @JoinColumn(name = "stadium_id", nullable = false)
    private StadiumEntity stadium;

    @Builder.Default
    @Column(name = "create_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
