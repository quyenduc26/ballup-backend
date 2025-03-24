package com.example.ballup_backend.service;

import java.util.List;
import java.util.stream.Collectors;

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
import com.example.ballup_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public void notifyUser(Long userId, String message) {
        messagingTemplate.convertAndSend("/topic/user/" + userId, message);
    }

    public void notifyOwner(Long ownerId, String message) {
        messagingTemplate.convertAndSend("/topic/owner/" + ownerId, message);
    }


    //create booking notificationfor user
    public void createUserBookingNotification(UserEntity user, BookingEntity booking, NotificationType type ) {
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

    //create team notificastion for user
    public void createUserTeamNotification(UserEntity user, TeamEntity team, NotificationType type ) {
        NotificationEntity notification = NotificationEntity.builder()
                .forUser(user)
                .type(type)
                .isRead(false)
                .team(team)
                .build();
        notification = notificationRepository.save(notification);
        NotificationResponse notificationResponse = NotificationResponse.builder()
            .id(notification.getId())
            .isRead(notification.isRead())
            .bookingId(notification.getBooking() != null ? notification.getBooking().getId() : null)
            .type(notification.getType())
            .build();
        messagingTemplate.convertAndSend("/topic/user/" + user.getId(), notificationResponse);
    }

    //create game notification for user
    public void createUserGameNotification(UserEntity user, GameEntity game, NotificationType type ) {
        NotificationEntity notification = NotificationEntity.builder()
                .forUser(user)
                .type(type)
                .isRead(false)
                .game(game)
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

    //create booking notification for owner
    public void createOwnerBookingNotification(UserEntity owner, BookingEntity booking, NotificationType type ) {
        NotificationEntity notification = NotificationEntity.builder()
                .forUser(owner)
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
        messagingTemplate.convertAndSend("/topic/owner/" + owner.getId(), notificationResponse);
    }

    public List<NotificationResponse> getAllNotifications(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository.findAllByUser(user).stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .isRead(notification.isRead())
                        .teamId(notification.getTeam() != null ? notification.getTeam().getId() : null)
                        .gameId(notification.getGame() != null ? notification.getGame().getId() : null)
                        .bookingId(notification.getBooking() != null ? notification.getBooking().getId() : null)
                        .type(notification.getType())
                        .createdAt(notification.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(List<Long> notiIdList) {
        List<NotificationEntity> notifications = notificationRepository.findAllById(notiIdList);

        if (notifications.isEmpty()) {
            throw new RuntimeException("No notifications found for the given IDs");
        }

        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    public void markAsReadByType(List<Long> notiIdList, NotificationType type) {
        List<NotificationEntity> notifications = notificationRepository.findAllById(notiIdList)
            .stream()
            .filter(noti -> noti.getType() == type)
            .collect(Collectors.toList());
    
        if (!notifications.isEmpty()) {
            notifications.forEach(noti -> noti.setRead(true));
            notificationRepository.saveAll(notifications);
        }
    }
    


}
