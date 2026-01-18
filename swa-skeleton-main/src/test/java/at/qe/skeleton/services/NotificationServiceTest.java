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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserxService userService;

    @Autowired
    SubscriptionService subscriptionService;

    Userx user;
    Product product;

    @BeforeEach
    void setup() {
        user = new Userx();
        user.setUsername("testUser");
        user.setPassword("pass");
        userService.saveUser(user);

        product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setStock(10L);
        product.setDiscount(0.0);
        product.setDescription("Initial");
        product.setImageUrl("test.jpg");
        product.setCategories(new HashSet<>(Set.of(ProductCategory.GB)));
        productService.saveProduct(product);

        subscriptionService.createSubscription(user, product);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testRestockNotification() {
        // Increase stock should trigger ProductRestockEvent notification
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, 20L, null, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertFalse(notifs.isEmpty());
        Assertions.assertTrue(notifs.stream().anyMatch(n -> n.getType() == NotificationType.RESTOCK));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testOutOfStockNotification() {
        // Set stock to 0 should trigger ProductOutOfStockEvent notification
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, 0L, null, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertFalse(notifs.isEmpty());
        Assertions.assertTrue(notifs.stream().anyMatch(n -> n.getType() == NotificationType.OUT_OF_STOCK));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testSaleNotification() {
        // Increase discount should trigger ProductSaleEvent notification
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, null, 0.3, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertFalse(notifs.isEmpty());
        Assertions.assertTrue(notifs.stream().anyMatch(n -> n.getType() == NotificationType.SALE));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testMultipleUpdatesProduceMultipleNotifications() {
        // Stock to 0 + discount increase triggers two notifications
        ProductDTO updated = new ProductDTO(
                product.getId(), null, null, null, 0L, 0.3, null, null
        );

        productService.updateProduct(product.getId(), updated);

        Collection<Notification> notifs = notificationService.getAllNotifcations();
        Assertions.assertEquals(2, notifs.size());
    }
}
