package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "playing-center")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayingCenterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(name = "image", length = 256)
    private String image;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "address", length = 256)
    private String address;
}
