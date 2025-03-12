package com.example.ballup_backend.entity;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true) 
    private String username;

    @Column(name = "email", nullable = false, length = 100, unique = true) 
    private String email;

    @Column(name = "phone", nullable = false, length = 100, unique = true) 
    private String phone;

    @Column(name = "first_name", nullable = true, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = true, length = 100)
    private String lastName;

    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Column(name = "google_id", nullable = true, length = 255)
    private String googleId;

    @Column(name = "avatar", nullable = true, length = 255 )
    private String avatar;

    @Column(name = "weight", nullable = true)
    private Integer weight;

    @Column(name = "height", nullable = true)
    private Integer height;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING) 
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    public enum Role {
        USER, OWNER, ADMIN
    }

}

