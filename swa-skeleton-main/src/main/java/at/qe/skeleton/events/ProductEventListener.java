package at.qe.skeleton.events;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.SubscriptionService;
import at.qe.skeleton.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

/**
 * Event listener that reacts to product-related domain events and
 * creates notifications for subscribed users.
 *
 * This class is part of the event-driven notification mechanism.
 */

@Component
public class ProductEventListener {

    private final SubscriptionService subscriptionService;
    private final NotificationService notificationService;
    private final ProductService productService;

    @Autowired
    public ProductEventListener(SubscriptionService subscriptionService, NotificationService notificationService, ProductService productService) {
        this.subscriptionService = subscriptionService;
        this.notificationService = notificationService;
        this.productService = productService;
    }

    /**
     * Handles a {@link ProductRestockEvent}.
     *
     * Creates and sends a RESTOCK notification to all users subscribed
     * to the affected product.
     *
     * @param event the restock event containing the product identifier
     */
    @EventListener
    public void handleRestock(ProductRestockEvent event) {
        Product product = productService.getProductById(event.getProductId()).orElseThrow();
        String title = product.getName() + " RESTOCK";
        String description = product.getName() + " was restocked! Stock is now " + product.getStock() + ".";
        notifySubscribers(product, title, description, NotificationType.RESTOCK);
    }

    /**
     * Handles a {@link ProductSaleEvent}.
     *
     * Creates and sends a SALE notification to all users subscribed
     * to the affected product.
     *
     * @param event the sale event containing the product identifier
     */
    @EventListener
    public void handleSale(ProductSaleEvent event) {
        Product product = productService.getProductById(event.getProductId()).orElseThrow();
        double price = product.getPrice();
        double discount = product.getDiscount();
        double newPrice = price * (1 - discount);

        String title = product.getName() + " SALE";
        String description = product.getName() + " is on sale! Price is now " + String.format("%.2f", newPrice)
                + " instead of " + String.format("%.2f", price) + ".";
        notifySubscribers(product, title, description, NotificationType.SALE);
    }

    /**
     * Handles a {@link ProductOutOfStockEvent}.
     *
     * Creates and sends an OUT_OF_STOCK notification to all users subscribed
     * to the affected product.
     *
     * @param event the out of stock event containing the product identifier
     */
    @EventListener
    public void handleOutOfStock(ProductOutOfStockEvent event) {
        Product product = productService.getProductById(event.getProductId()).orElseThrow();

        String title = product.getName() + " OUT OF STOCK";
        String description = product.getName() + " is out of stock! You will be notified about restocks.";
        notifySubscribers(product, title, description, NotificationType.OUT_OF_STOCK);
    }

    /**
     * Notifies all users subscribed to the given product.
     *
     * @param product the product related to the notification
     * @param title the notification title
     * @param description the notification message
     * @param type the notification type
     */
    private void notifySubscribers(Product product, String title, String description, NotificationType type) {
        subscriptionService.getSubscriptionByProductId(product.getId())
            .forEach(sub -> {
                Userx user = sub.getUser();

                Notification notification = new Notification();
                notification.setTitle(title);
                notification.setDescription(description);
                notification.setProduct(product);
                notification.setTimestamp(LocalDateTime.now());
                notification.setType(type);
                notification.setUser(user);

                notificationService.saveNotification(notification);
                notificationService.sendNotification(user, title, description);
            });
    }
}
