package at.qe.skeleton.tests;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.AuthenticatedUserService;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.SubscriptionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

@SpringBootTest
@Transactional
public class SubscriptionServiceTest {

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    ProductService productService;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    Product product1;
    Product product2;

    @BeforeEach
    void setup() {
        product1 = new Product();
        product1.setName("Test Product 1");
        product1.setStock(2L);
        product1.setPrice(10.0);
        product1.setDiscount(0.0);
        product1.setDescription("Test");
        product1.setCategories(new HashSet<>(Set.of(ProductCategory.GB)));
        product1.setImageUrl("test.jpg");
        productService.saveProduct(product1);

        product2 = new Product();
        product2.setName("Test Product 2");
        product2.setStock(5L);
        product2.setPrice(20.0);
        product2.setDiscount(0.0);
        product2.setDescription("Test");
        product2.setCategories(new HashSet<>(Set.of(ProductCategory.N64)));
        product2.setImageUrl("test.jpg");
        productService.saveProduct(product2);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    public void testCreateSubscription() {
        productService.saveProduct(product1);
        productService.saveProduct(product2);

        Userx user = authenticatedUserService.getAuthenticatedUser();

        Subscription sub1 = subscriptionService.createSubscription(user, product1);
        Subscription sub2 = subscriptionService.createSubscription(user, product2);

        Assertions.assertNotNull(sub1);
        Assertions.assertNotNull(sub2);

        Collection<Subscription> subscriptions = subscriptionService.getSubscriptionByUserId(user.getId());

        Assertions.assertEquals(2, subscriptions.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    public void testDuplicateSubscriptionNotCreated() {
        Userx user = authenticatedUserService.getAuthenticatedUser();

        Subscription first = subscriptionService.createSubscription(user, product1);
        Subscription second = subscriptionService.createSubscription(user, product1);

        Assertions.assertEquals(first.getId(), second.getId());

        Collection<Subscription> subscriptions = subscriptionService.getSubscriptionByUserId(user.getId());

        Assertions.assertEquals(1, subscriptions.size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    public void testGetSubscriptionByUserAndProduct() {
        Userx user = authenticatedUserService.getAuthenticatedUser();

        subscriptionService.createSubscription(user, product1);

        Subscription subscription = subscriptionService.getSubscriptionByUserIdAndProductId(user.getId(), product1.getId());

        Assertions.assertNotNull(subscription);
        Assertions.assertEquals(user, subscription.getUser());
        Assertions.assertEquals(product1, subscription.getProduct());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    public void testDeleteSubscription() {
        Userx user = authenticatedUserService.getAuthenticatedUser();

        Collection<Subscription> subscriptions =
                subscriptionService.getSubscriptionByUserId(user.getId());

        for (Subscription s : subscriptions) {
            subscriptionService.deleteSubscription(s);
        }

        Assertions.assertTrue(
                subscriptionService.getSubscriptionByUserId(user.getId()).isEmpty()
        );
    }
}
