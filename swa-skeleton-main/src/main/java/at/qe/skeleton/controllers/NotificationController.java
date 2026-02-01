package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.NotificationDTO;
import at.qe.skeleton.mappers.NotificationMapper;
import at.qe.skeleton.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

import org.springframework.http.ResponseEntity;

/**
 * Notification endpoints exposed by the server.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private NotificationService notificationService;

    private NotificationMapper notificationMapper;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Collection<NotificationDTO>> getAllNotificationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId)
                .stream()
                .map(n -> notificationMapper.mapTo(n))
                .toList());
    }
}
