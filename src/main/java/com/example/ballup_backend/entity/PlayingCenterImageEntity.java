package com.example.ballup_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "playing-center-images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayingCenterImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = false)
    private PlayingCenterEntity center;

    @Column(name = "image", nullable = false, length = 500)
    private String image;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
