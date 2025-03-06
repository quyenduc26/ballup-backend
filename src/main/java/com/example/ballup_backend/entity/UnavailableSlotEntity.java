package com.example.ballup_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "unavailable-slot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnavailableSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time", nullable = false)
    private Timestamp fromTime;

    @Column(name = "return_time", nullable = false)
    private Timestamp toTime;

    @ManyToOne
    @JoinColumn(name = "playing_slot_id", nullable = false)
    private PlayingSlotEntity slot ;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private UserEntity creator;

    @Enumerated(EnumType.STRING)
    @Column(name = "create_by", nullable = false)
    private createdBy createBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public enum createdBy {
        BY_USER, BY_OWNER
    }

    public enum Status {
        SUBMITTING, PROCESSING, PENDING, DONE
    }

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
