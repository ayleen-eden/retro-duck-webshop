package at.qe.skeleton.services;

import at.qe.skeleton.model.MailCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StubMailService {

    private Logger log = LoggerFactory.getLogger(StubMailService.class);

    public void sendMail(String to, String subject, String body, MailCategory category) {
        log.info("===== STUB MAIL OF CATEGORY " + category.toString() + " =====");
        log.info("To: " + to);
        log.info("Subject: " + subject);
        log.info("Body: \n" + body);
        log.info("===================================================");
    }

}
