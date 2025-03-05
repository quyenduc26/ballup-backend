package com.example.ballup_backend.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "address", length = 256)
    private String address;

    @Column(name = "intro", length = 256)
    private String intro;

    @Column(name = "logo", length = 256)
    private String logo;

    @Column(name = "cover", length = 256)
    private String cover;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private ConversationEntity conversation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Sport sport;


    public enum Sport {
        FOOTBALL, BADMINTON
    }

    
}
