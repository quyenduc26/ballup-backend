package com.example.ballup_backend.entity;import java.security.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team-member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
