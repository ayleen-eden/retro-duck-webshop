package at.qe.skeleton.tests;

import at.qe.skeleton.model.Notification;
import at.qe.skeleton.model.NotificationType;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.NotificationService;
import at.qe.skeleton.services.NotificationChannelType;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@WithMockUser(username = "admin", authorities = {"ADMIN"})
class NotificationServiceFullTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserxService userxService;

    private Userx testUser;

    @BeforeEach
    void setUp() {
        testUser = new Userx();
        testUser.setUsername("test_pro");
        testUser.setEmail("pro@test.at");
        testUser.setFirstName("Test");
        testUser.setLastName("Pro");
        testUser.setPassword("password");
//        testUser.setPreferredChannels(Set.of(
//                NotificationChannelType.EMAIL,
//                NotificationChannelType.WHATSAPP
//        ));
        userxService.saveUser(testUser);
    }

    @Test
    void testSaveNotification() {
        Notification note = createBaseNotification("Test Title");

        Notification saved = notificationService.saveNotification(note);
        Notification fetched = notificationService.getNotificationById(saved.getId());

        assertNotNull(fetched);
        assertEquals(saved.getId(), fetched.getId());
        assertEquals(note.getTitle(), fetched.getTitle());
        assertEquals(note.getDescription(), fetched.getDescription());
        assertEquals(note.getProduct(), fetched.getProduct());
        assertEquals(note.getType(), fetched.getType());
        assertEquals(note.getUser(), fetched.getUser());
    }

    @Test
    void testGetNotificationByAttributes() {
        notificationService.saveNotification(createBaseNotification("Notification Title"));

        Collection<Notification> byType = notificationService.getNotificationsByType(NotificationType.RESTOCK);
        Collection<Notification> byUser = notificationService.getNotificationsByUserId(testUser.getId());
        Collection<Notification> byTime = notificationService.getNotificationsBetween(LocalDateTime.now().minusHours(1), LocalDateTime.now());

        assertTrue(byType.stream().allMatch(n -> n.getType() == NotificationType.RESTOCK));
        assertTrue(byUser.stream().allMatch(n -> n.getUser().getId().equals(testUser.getId())));
        assertTrue(byTime.stream().allMatch(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusHours(1))));
    }

    @Test
    void testDeleteNotification() {
        Notification saved = notificationService.saveNotification(createBaseNotification("To be deleted"));

        notificationService.deleteNotification(saved);

        assertNull(notificationService.getNotificationById(saved.getId()));
    }

    @Test
    void testDefaultChannel() {
        assertEquals(Set.of(NotificationChannelType.EMAIL), testUser.getPreferredChannels());
    }

    private Notification createBaseNotification(String title) {
        Notification note = new Notification();
        note.setTitle(title);
        note.setDescription("Description for " + title);
        note.setType(NotificationType.RESTOCK);
        note.setUser(testUser);
        note.setTimestamp(LocalDateTime.now());
        return note;
    }
}