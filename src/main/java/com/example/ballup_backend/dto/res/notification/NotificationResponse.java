package com.example.ballup_backend.dto.res.notification;
import com.example.ballup_backend.entity.NotificationEntity.NotificationType;

import jakarta.annotation.Nullable;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private boolean isRead;

    @Nullable
    private Long teamId;

    @Nullable
    private Long gameId;

    @Nullable
    private Long bookingId;

    private NotificationType type;
    private Timestamp createdAt;
}
