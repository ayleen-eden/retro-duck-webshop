package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.SubscriptionDTO;
import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.SubscriptionService;
import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mapping between SubscriptionDTO and Subscription.
 */
@Service
public class SubscriptionMapper implements DTOMapper<Subscription, SubscriptionDTO> {

    public UserxService userxService;

    public SubscriptionService subscriptionService;

    public ProductService productService;

    @Autowired
    public SubscriptionMapper(UserxService userxService, SubscriptionService subscriptionService, ProductService productService) {
        this.userxService = userxService;
        this.subscriptionService = subscriptionService;
        this.productService = productService;
    }

    @Override
    public SubscriptionDTO mapTo(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        return new SubscriptionDTO(
            subscription.getId(),
            subscription.getUser().getId(),
            subscription.getProduct().getId()
        );
    }

    @Override
    public Subscription mapFrom(SubscriptionDTO dto) {
        if (dto == null) return null;

        Subscription sub;
        if (dto.id() != null) {
            sub = subscriptionService.getSubscriptionById(dto.id()).get();
        } else {
            sub = new Subscription();
        }

        sub.setId(dto.id());
        sub.setUser(userxService.loadUser(dto.userId()).orElse(null));
        sub.setProduct(productService.getProductById(dto.productId()).orElse(null));

        return sub;
    }

}
