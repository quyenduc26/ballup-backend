package com.example.ballup_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.res.notification.NotificationResponse;
import com.example.ballup_backend.entity.NotificationEntity.NotificationType;
import com.example.ballup_backend.service.NotificationService;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestParam Long userId, @RequestParam Long ownerId ) {        
        notificationService.notifyUser(userId, "Your booking is confirmed!");
        notificationService.notifyOwner(ownerId, "A new booking has been made at your center.");
        
        return ResponseEntity.ok("Ngon luônnnn");
    }

    @GetMapping("/{userId}")
    public List<NotificationResponse> getAllNotifications(@PathVariable Long userId) {
        return notificationService.getAllNotifications(userId);
    }

    @PatchMapping("/read")
    public ResponseEntity<String> markNotificationsAsRead(@RequestBody List<Long> notiIdList) {
        notificationService.markAsRead(notiIdList);
        return ResponseEntity.ok("Notifications marked as read");
    }

    @PatchMapping("/read/booking-requested")
    public ResponseEntity<String> markOwnerBookingRequestedAsRead(@RequestBody List<Long> notiIdList) {
        notificationService.markAsReadByType(notiIdList, NotificationType.BOOKING_REQUESTED);
        return ResponseEntity.ok("Owner's Booking Requested notifications marked as read");
    }

    @PatchMapping("/read/booking-deposited")
    public ResponseEntity<String> markOwnerBookingDepositedAsRead(@RequestBody List<Long> notiIdList) {
        notificationService.markAsReadByType(notiIdList, NotificationType.BOOKING_DEPOSITED);
        return ResponseEntity.ok("Owner's Booking Deposited notifications marked as read");
    }

}
