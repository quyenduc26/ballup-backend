package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "playing-slot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayingSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "primary_price", nullable = false)
    private Integer primaryPrice;

    @Column(name = "night_price", nullable = false)
    private Integer nightPrice;

    @ManyToOne
    @JoinColumn(name = "playing_center_id", nullable = false)
    private PlayingCenterEntity playingCenter ;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
