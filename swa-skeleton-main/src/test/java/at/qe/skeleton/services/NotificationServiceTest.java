package at.qe.skeleton.services;

import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.model.*;
import at.qe.skeleton.services.NotificationService;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.SubscriptionService;
import at.qe.skeleton.services.UserxService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@WithMockUser(username = "admin", authorities = {"ADMIN"})
public class NotificationServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    private UserxService userxService;

    @Autowired
    SubscriptionService subscriptionService;

    private Userx testUser;
    Product product;

    @BeforeEach
    void setup() {
        testUser = new Userx();
        testUser.setUsername("test_pro");
        testUser.setEmail("pro@test.at");
        testUser.setFirstName("Test");
        testUser.setLastName("Pro");
        testUser.setPassword("password");
        userxService.saveUser(testUser);

        product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setStock(10L);
        product.setDiscount(0.0);
        product.setDescription("Initial");
        product.setImageUrl("test.jpg");
        product.setCategories(new HashSet<>(Set.of(ProductCategory.GB)));
        productService.saveProduct(product);

        subscriptionService.createSubscription(testUser, product);
    }

    @Test
    public void testRestockNotification() {
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, 20L, null, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertFalse(notifs.isEmpty());
        Assertions.assertTrue(notifs.stream().anyMatch(n -> n.getType() == NotificationType.RESTOCK));
    }

    @Test
    public void testOutOfStockNotification() {
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, 0L, null, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertFalse(notifs.isEmpty());
        Assertions.assertTrue(notifs.stream().anyMatch(n -> n.getType() == NotificationType.OUT_OF_STOCK));
    }

    @Test
    public void testSaleNotification() {
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, null, 0.3, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertFalse(notifs.isEmpty());
        Assertions.assertTrue(notifs.stream().anyMatch(n -> n.getType() == NotificationType.SALE));
    }

    @Test
    public void testMultipleUpdatesProduceMultipleNotifications() {
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, 0L, 0.3, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertEquals(2, notifs.size());
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
