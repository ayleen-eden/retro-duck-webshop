package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SMSNotificationChannel implements NotificationChannel {

    private Logger log = LoggerFactory.getLogger(SMSNotificationChannel.class);

    public NotificationChannelType getType() {
        return NotificationChannelType.SMS;
    }

    public void send(Userx user, String title, String message) {
        log.info("=STUB SMS NOTIFICATION=============================");
        log.info("To: " + user.getPhone());
        log.info("Subject: " + title);
        log.info("Body: \n" + message);
        log.info("===================================================");
    }
}
