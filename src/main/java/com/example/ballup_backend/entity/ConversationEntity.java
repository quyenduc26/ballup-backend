package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}

