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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

/**
 * Subscription endpoints exposed by the server.
 */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    private SubscriptionMapper subscriptionMapper;

    private ProductService productService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, ProductService productService, SubscriptionMapper subscriptionMapper) {
        this.subscriptionService = subscriptionService;
        this.subscriptionMapper = subscriptionMapper;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> subscribe(@RequestParam Long productId) {

        Userx user = (Userx) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Product> productOpt = productService.getProductById(productId);

        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Subscription subscription = subscriptionService.createSubscription(user, productOpt.get());

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(
                subscription.getId(),
                subscription.getUser().getId(),
                subscription.getProduct().getId()
        );

        return ResponseEntity.ok(subscriptionDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> unsubscribe(@RequestParam Long productId) {

        Userx user = (Userx) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productService.getProductById(productId).orElse(null);

        if (user == null || product == null) {
            return ResponseEntity.notFound().build();
        }

        subscriptionService.deleteSubscription(subscriptionService.getSubscriptionByUserIdAndProductId(user.getId(), productId));

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
