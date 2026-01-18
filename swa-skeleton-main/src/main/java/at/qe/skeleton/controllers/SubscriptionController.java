package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.SubscriptionDTO;
import at.qe.skeleton.mappers.SubscriptionMapper;
import at.qe.skeleton.services.SubscriptionService;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.UserxService;
import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Userx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    private SubscriptionMapper subscriptionMapper;

    private ProductService productService;

    private UserxService userService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, ProductService productService, UserxService userService, SubscriptionMapper subscriptionMapper) {
        this.subscriptionService = subscriptionService;
        this.subscriptionMapper = subscriptionMapper;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> subscribe(@RequestParam Long userId, @RequestParam Long productId) {

        Optional<Userx> userOpt = userService.loadUser(userId);
        Optional<Product> productOpt = productService.getProductById(productId);

        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Subscription subscription = subscriptionService.createSubscription(userOpt.get(), productOpt.get());

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(
                subscription.getId(),
                subscription.getUser().getId(),
                subscription.getProduct().getId()
        );

        return ResponseEntity.ok(subscriptionDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> unsubscribe(@RequestParam Long userId, @RequestParam Long productId) {
        Userx user = userService.loadUser(userId).orElse(null);
        Product product = productService.getProductById(productId).orElse(null);

        if (user == null || product == null) {
            return ResponseEntity.notFound().build();
        }

        subscriptionService.deleteSubscription(subscriptionService.getSubscriptionByUserIdAndProductId(userId, productId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Collection<SubscriptionDTO>> getAllSubscriptionsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionByUserId(userId)
                .stream()
                .map(s -> subscriptionMapper.mapTo(s))
                .toList());
    }
}
