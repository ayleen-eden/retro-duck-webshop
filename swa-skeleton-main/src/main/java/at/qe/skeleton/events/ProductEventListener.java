package at.qe.skeleton.events;

import at.qe.skeleton.model.Notification;
import at.qe.skeleton.model.NotificationType;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.services.ProductService;
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

    @Autowired
    public ProductEventListener(SubscriptionService subscriptionService, NotificationService notificationService, ProductService productService) {
        this.subscriptionService = subscriptionService;
        this.notificationService = notificationService;
        this.productService = productService;
    }

    @EventListener
    public void handleRestock(ProductRestockEvent event) {

        Product product = productService.getProductById(event.getProductId()).orElseThrow();
        String productName = product.getName();
        Long productStock = product.getStock();

        Collection<Subscription> subs = subscriptionService.getSubscriptionByProductId(product.getId());

        for (Subscription sub : subs) {
            Notification notification = new Notification();
            notification.setTitle(String.format("%s restock", productName));
            notification.setDescription(String.format("%s was restocked. Stock is now %d", productName, productStock));
            notification.setProduct(product);
            notification.setTimestamp(LocalDateTime.now());
            notification.setType(NotificationType.RESTOCK);
            notification.setUser(sub.getUser());

            notificationService.saveNotification(notification);
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
            notification.setTitle(String.format("%s sale", productName));
            notification.setDescription(String.format("%s is on sale. Price is now %.2f instead of %.2f.", productName, newProductPrice, productPrice));
            notification.setProduct(product);
            notification.setTimestamp(LocalDateTime.now());
            notification.setType(NotificationType.SALE);
            notification.setUser(sub.getUser());

            notificationService.saveNotification(notification);
        }
    }

    @EventListener
    public void handleOutOfStock(ProductOutOfStockEvent event) {

        Product product = productService.getProductById(event.getProductId()).orElseThrow();
        String productName = product.getName();

        Collection<Subscription> subs = subscriptionService.getSubscriptionByProductId(product.getId());

        for (Subscription sub : subs) {
            Notification notification = new Notification();
            notification.setTitle(String.format("%s out of stock", productName));
            notification.setDescription(String.format("%s is out of stock. You will be notified about restocks.", productName));
            notification.setProduct(product);
            notification.setTimestamp(LocalDateTime.now());
            notification.setType(NotificationType.OUT_OF_STOCK);
            notification.setUser(sub.getUser());

            notificationService.saveNotification(notification);
        }
    }
}
