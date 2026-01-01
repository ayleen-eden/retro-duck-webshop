package at.qe.skeleton.services;

import at.qe.skeleton.model.Notification;
import at.qe.skeleton.model.NotificationType;
import at.qe.skeleton.repositories.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class NotificationService {

    NotificationRepository notificationRepository;

    @Autowired
    private NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Notification> getAllNotifcations() {
        return notificationRepository.findAll();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Notification> getNotifcationsByType(NotificationType type) {
        return notificationRepository.findByType(type);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Notification> getNotificationsBetween(LocalDateTime from, LocalDateTime to) {
        return notificationRepository.findByTimestampBetween(from, to);
    }
}
