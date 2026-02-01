package at.qe.skeleton.services;

import at.qe.skeleton.model.Notification;
import at.qe.skeleton.model.NotificationType;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class NotificationService {

    NotificationRepository notificationRepository;

    private final List<NotificationChannel> channels;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, List<NotificationChannel> channels) {
        this.notificationRepository = notificationRepository;
        this.channels = channels;
    }

    /**
     * Returns all stored Notification.
     *
     * @return collection of all Notifications
     */
    public Collection<Notification> getAllNotifcations() {
        return notificationRepository.findAll();
    }

    /**
     * Saves the Notification. This method will also set {@link Notification#timestamp} for new
     * and updated entities.
     *
     * @param notification Notification to save
     * @return the updated Notification
     */
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * Retrieves a Notification by its identifier.
     *
     * @param id the Notification ID
     * @return the Notification, or {@code null} if not found
     */
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    /**
     * Deletes the Notification.
     *
     * @param notification the Notification to delete
     */
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    /**
     * Retrieves all Notifications of a given type.
     *
     * @param type the Notification type
     * @return Notifications matching the given type
     */
    public Collection<Notification> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByType(type);
    }

    /**
     * Retrieves all Notifications within a time interval.
     *
     * @param from the start of the interval
     * @param to the end of the interval
     * @return Notifications matching the given interval
     */
    public Collection<Notification> getNotificationsBetween(LocalDateTime from, LocalDateTime to) {
        return notificationRepository.findByTimestampBetween(from, to);
    }

    /**
     * Retrieves all Notifications for a given user.
     *
     * @param id the user id
     * @return Notifications for the user
     */
    public Collection<Notification> getNotificationsByUserId(Long id) {
        return notificationRepository.findByUserId(id);
    }

    /**
     * Sends a Notification to the given user using their preferred channels.
     *
     * @param user the recipient of the Notification
     * @param title the Notification title
     * @param message the Notification message
     */
    public void sendNotification(Userx user, String title, String message) {
        for (NotificationChannel channel : channels) {
            if (user.getPreferredChannels() != null &&
                    user.getPreferredChannels().contains(channel.getType())) {
                channel.send(user, title, message);
            }
        }
    }
}
