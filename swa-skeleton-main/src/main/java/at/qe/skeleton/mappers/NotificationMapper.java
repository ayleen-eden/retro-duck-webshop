package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.NotificationDTO;
import at.qe.skeleton.model.Notification;
import at.qe.skeleton.services.NotificationService;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mapping between NotificationDTO and Notification.
 */
@Service
public class NotificationMapper implements DTOMapper<Notification, NotificationDTO> {

    private NotificationService notificationService;

    private UserxService userxService;

    private ProductService productService;

    @Autowired
    public NotificationMapper(NotificationService notificationService, UserxService userxService, ProductService productService) {
        this.notificationService = notificationService;
        this.userxService = userxService;
        this.productService = productService;
    }

    @Override
    public NotificationDTO mapTo(Notification notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationDTO(
            notification.getId(),
            notification.getProduct().getId(),
            notification.getUser().getId(),
            notification.getDescription(),
            notification.getTitle(),
            notification.getTimestamp(),
            notification.getType()
        );
    }

    @Override
    public Notification mapFrom(NotificationDTO notificationDTO) {
        if (null == notificationDTO) {
            return null;
        }
        Notification notification;
        if (null != notificationDTO.id()) {
            notification = notificationService.getNotificationById(notificationDTO.id());
        } else {
            notification = new Notification();
        }

        notification.setUser(userxService.loadUser(notificationDTO.userId()).orElse(null));
        notification.setProduct(productService.getProductById(notificationDTO.productId()).orElse(null));
        notification.setDescription(notificationDTO.description());
        notification.setTitle(notificationDTO.title());
        notification.setTimestamp(notificationDTO.timestamp());
        notification.setType(notificationDTO.type());

        return notification;
    }
}
