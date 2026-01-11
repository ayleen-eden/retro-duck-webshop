package at.qe.skeleton.services;

import at.qe.skeleton.model.Notification;
import at.qe.skeleton.model.NotificationType;
import at.qe.skeleton.repositories.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class NotificationService {

    NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Collection<Notification> getAllNotifcations() {
        return notificationRepository.findAll();
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    public Collection<Notification> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByType(type);
    }

    public Collection<Notification> getNotificationsBetween(LocalDateTime from, LocalDateTime to) {
        return notificationRepository.findByTimestampBetween(from, to);
    }

    public Collection<Notification> getNotificationsByUserId(Long id) {
        return notificationRepository.findByUserId(id);
    }
}
