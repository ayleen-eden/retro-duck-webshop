package at.qe.skeleton.services;

import at.qe.skeleton.model.MailCategory;
import org.springframework.stereotype.Service;

@Service
public class StubMailService {

    public void sendMail(String to, String subject, String body, MailCategory category) {
        System.out.println("===== STUB MAIL OF CATEGORY " + category.toString() + " =====");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: \n" + body);
        System.out.println("===================================================");
    }

}
