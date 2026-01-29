package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MailNotificationChannel implements NotificationChannel {

    private Logger log = LoggerFactory.getLogger(MailNotificationChannel.class);

    public NotificationChannelType getType() {
        return NotificationChannelType.EMAIL;
    }

    public void send(Userx user, String title, String message) {
        log.info("=STUB EMAIL NOTIFICATION===========================");
        log.info("To: " + user.getEmail());
        log.info("Subject: " + title);
        log.info("Body: \n" + message);
        log.info("===================================================");
    }
}
