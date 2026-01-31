package at.qe.skeleton.services;

import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Saves the Subscription.
     *
     * @param subscription Subscription to save
     * @return the updated Subscription
     */
    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    /**
     * Retrieves a Subscription by its identifier.
     *
     * @param id the Subscription ID
     * @return the Subscription, or {@code null} if not found
     */
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    /**
     * Deletes the Subscription.
     *
     * @param subscription the Subscription to delete
     */
    public void deleteSubscription(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    /**
     * Returns all stored Subscriptions.
     *
     * @return collection of all Subscriptions
     */
    public Collection<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    /**
     * Retrieves all Subscriptions for a given user.
     *
     * @param id the user id
     * @return Subscriptions for the user
     */
    public Collection<Subscription> getSubscriptionByUserId(Long id) {
        return subscriptionRepository.findByUserId(id);
    }

    /**
     * Retrieves all Subscriptions for a given product.
     *
     * @param id the product id
     * @return Subscriptions for the product
     */
    public Collection<Subscription> getSubscriptionByProductId(Long id) {
        return subscriptionRepository.findByProductId(id);
    }

    /**
     * Retrieves all Subscriptions for a given product and user.
     *
     * @param userId the user id
     * @param productId the product id
     * @return Subscriptions for the product and user
     */
    public Subscription getSubscriptionByUserIdAndProductId(Long userId, Long productId) {
        return subscriptionRepository.findByUserIdAndProductId(userId, productId);
    }

    /**
     * Creates a Subscriptions for a given product and user.
     * Prevents duplicates.
     *
     * @param user the user
     * @param product the product
     * @return the new Subscription
     */
    public Subscription createSubscription(Userx user, Product product) {
        Subscription subscription = getSubscriptionByUserIdAndProductId(user.getId(), product.getId());

        if (subscription == null) {
            Subscription sub = new Subscription();
            sub.setUser(user);
            sub.setProduct(product);
            return subscriptionRepository.save(sub);
        } else {
            return subscription;
        }
    }
}
