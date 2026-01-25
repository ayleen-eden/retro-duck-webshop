package at.qe.skeleton.events;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.StubMailService;
import at.qe.skeleton.services.SubscriptionService;
import at.qe.skeleton.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class ProductEventListener {

    private SubscriptionService subscriptionService;

    private NotificationService notificationService;

    private ProductService productService;

    private StubMailService stubMailService;

    @Autowired
    public ProductEventListener(SubscriptionService subscriptionService, NotificationService notificationService, ProductService productService, StubMailService stubMailService) {
        this.subscriptionService = subscriptionService;
        this.notificationService = notificationService;
        this.productService = productService;
        this.stubMailService = stubMailService;
    }

    @EventListener
    public void handleRestock(ProductRestockEvent event) {

        Product product = productService.getProductById(event.getProductId()).orElseThrow();
        String productName = product.getName();
        Long productStock = product.getStock();

        Collection<Subscription> subs = subscriptionService.getSubscriptionByProductId(product.getId());

        for (Subscription sub : subs) {
            Notification notification = new Notification();
            Userx user = sub.getUser();

            String title = String.format("%s RESTOCK", productName);
            String description = String.format("%s was restocked!\nStock is now %d.", productName, productStock);

            notification.setTitle(title);
            notification.setDescription(description);
            notification.setProduct(product);
            notification.setTimestamp(LocalDateTime.now());
            notification.setType(NotificationType.RESTOCK);
            notification.setUser(user);

            notificationService.saveNotification(notification);

            stubMailService.sendMail((user.getFirstName() + " " + user.getLastName()), title, description, MailCategory.NOTIFICATION);
        }
    }

    @EventListener
    public void handleSale(ProductSaleEvent event) {

        Product product = productService.getProductById(event.getProductId()).orElseThrow();
        String productName = product.getName();
        Double productPrice = product.getPrice();
        Double productDiscount = product.getDiscount();
        Double newProductPrice = productPrice * (1 - productDiscount);

        Collection<Subscription> subs = subscriptionService.getSubscriptionByProductId(product.getId());

        for (Subscription sub : subs) {
            Notification notification = new Notification();
            Userx user = sub.getUser();

            String title = String.format("%s SALE", productName);
            String description = String.format("%s is on sale!\nPrice is now %.2f instead of %.2f.", productName, newProductPrice, productPrice);

            notification.setTitle(title);
            notification.setDescription(description);
            notification.setProduct(product);
            notification.setTimestamp(LocalDateTime.now());
            notification.setType(NotificationType.SALE);
            notification.setUser(user);

            notificationService.saveNotification(notification);

            stubMailService.sendMail((user.getFirstName() + " " + user.getLastName()), title, description, MailCategory.NOTIFICATION);
        }
    }

    @EventListener
    public void handleOutOfStock(ProductOutOfStockEvent event) {

        Product product = productService.getProductById(event.getProductId()).orElseThrow();
        String productName = product.getName();

        Collection<Subscription> subs = subscriptionService.getSubscriptionByProductId(product.getId());

        for (Subscription sub : subs) {
            Notification notification = new Notification();
            Userx user = sub.getUser();

            String title = String.format("%s OUT OF STOCK", productName);
            String description = String.format("%s is out of stock!\nYou will be notified about restocks.", productName);

            notification.setTitle(title);
            notification.setDescription(description);
            notification.setProduct(product);
            notification.setTimestamp(LocalDateTime.now());
            notification.setType(NotificationType.OUT_OF_STOCK);
            notification.setUser(user);

            notificationService.saveNotification(notification);

            stubMailService.sendMail((user.getFirstName() + " " + user.getLastName()), title, description, MailCategory.NOTIFICATION);
        }
    }
}
