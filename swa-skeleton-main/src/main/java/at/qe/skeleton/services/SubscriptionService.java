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

    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public void deleteSubscription(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    public Collection<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Collection<Subscription> getSubscriptionByUserId(Long id) {
        return subscriptionRepository.findByUserId(id);
    }

    public Collection<Subscription> getSubscriptionByProductId(Long id) {
        return subscriptionRepository.findByProductId(id);
    }

    public Subscription getSubscriptionByUserIdAndProductId(Long userId, Long productId) {
        return subscriptionRepository.findByUserIdAndProductId(userId, productId);
    }

    public void deleteSubscriptionById(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

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
