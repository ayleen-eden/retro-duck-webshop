package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.dtos.SubscriptionDTO;
import at.qe.skeleton.dtos.UserxDTO;
import at.qe.skeleton.services.SubscriptionService;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.UserxService;
import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Userx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    private ProductService productService;

    private UserxService userService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, ProductService productService, UserxService userService) {
        this.subscriptionService = subscriptionService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<SubscriptionDTO> subscribe(@RequestParam UserxDTO userDTO, @RequestParam ProductDTO productDTO) {

        Optional<Userx> user = userService.loadUser(userDTO.id());
        Optional<Product> product = productService.getProductById(productDTO.id());

        if (user.isEmpty() || product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Subscription subscription = subscriptionService.createSubscription(user.get(), product.get());

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(
                subscription.getId(),
                subscription.getUser().getId(),
                subscription.getProduct().getId()
        );

        return ResponseEntity.ok(subscriptionDTO);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> unsubscribe(@RequestParam Long userId, @RequestParam Long productId) {
        Userx user = userService.loadUser(userId).orElse(null);
        Product product = productService.getProductById(productId).orElse(null);

        if (user == null || product == null) {
            return ResponseEntity.notFound().build();
        }

        subscriptionService.deleteSubscription(subscriptionService.getSubscriptionByUserAndProduct(user, product).orElse(null));

        return ResponseEntity.noContent().build();
    }
}
