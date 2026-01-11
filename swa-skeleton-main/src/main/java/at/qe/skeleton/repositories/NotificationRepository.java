package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Notification;
import at.qe.skeleton.model.NotificationType;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface NotificationRepository extends AbstractRepository<Notification, Long> {

    Collection<Notification> findByType(NotificationType type);

    Collection<Notification> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    Collection<Notification> findByUserId(Long id);
}
