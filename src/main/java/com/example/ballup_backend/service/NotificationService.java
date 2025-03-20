package com.example.ballup_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.res.notification.NotificationResponse;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.NotificationEntity;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.NotificationEntity.NotificationType;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    public void notifyUser(Long userId, String message) {
        messagingTemplate.convertAndSend("/topic/user/" + userId, message);
    }

    public void notifyOwner(Long ownerId, String message) {
        messagingTemplate.convertAndSend("/topic/owner/" + ownerId, message);
    }

    public void createUserBookingNotification(UserEntity user, String message, BookingEntity booking, NotificationType type ) {
        NotificationEntity notification = NotificationEntity.builder()
                .forUser(user)
                .type(type)
                .isRead(false)
                .booking(booking)
                .build();
        notification = notificationRepository.save(notification);

        NotificationResponse notificationResponse = NotificationResponse.builder()
            .id(notification.getId())
            .isRead(notification.isRead())
            .bookingId(notification.getBooking().getId())
            .type(notification.getType())
            .build();
        messagingTemplate.convertAndSend("/topic/user/" + user.getId(), notificationResponse);
    }

    public void createUserTeamNotification(UserEntity user, String message, TeamEntity team, NotificationType type ) {
        NotificationEntity notification = NotificationEntity.builder()
                .forUser(user)
                .type(type)
                .isRead(false)
                .team(team)
                .build();
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/user/" + user.getId(), message);
    }

    public void createUserGameNotification(UserEntity user, String message, GameEntity game, NotificationType type ) {
        NotificationEntity notification = NotificationEntity.builder()
                .forUser(user)
                .type(type)
                .isRead(false)
                .game(game)
                .build();
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/user/" + user.getId(), message);
    }

    public void createOwnerBookingNotification(UserEntity owner, String message, BookingEntity booking, NotificationType type ) {
        NotificationEntity notification = NotificationEntity.builder()
                .forUser(owner)
                .type(type)
                .isRead(false)
                .booking(booking)
                .build();
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/user/" + owner.getId(), message);
    }

}
