package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link NotificationChannel} implementation that simulates
 * whatsapp delivery.
 *
 * The channel type supported by this implementation is {@link NotificationChannelType#WHATSAPP}.
 */
@Component
public class WhatsAppNotificationChannel implements NotificationChannel {

    private Logger log = LoggerFactory.getLogger(WhatsAppNotificationChannel.class);

    /**
     * Returns the notification channel type supported by this implementation.
     *
     * @return {@link NotificationChannelType#EMAIL}
     */
    public NotificationChannelType getType() {
        return NotificationChannelType.WHATSAPP;
    }

    /**
     * Sends a notification to the specified user.
     *
     * @param user the recipient
     * @param title the notification title
     * @param message the notification message
     */
    public void send(Userx user, String title, String message) {
        log.info("=STUB WHATSAPP NOTIFICATION========================");
        log.info("To: " + user.getFirstName() + " " + user.getLastName());
        log.info("Phone number: " + user.getPhone());
        log.info("Subject: " + title);
        log.info("Body: " + message);
        log.info("===================================================");
    }
}
