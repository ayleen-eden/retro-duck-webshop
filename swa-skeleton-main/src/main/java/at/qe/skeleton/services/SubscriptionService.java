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

    public Collection<Subscription> getSubscriptionByUser(Userx user) {
        return subscriptionRepository.findByUser(user);
    }

    public Collection<Subscription> getSubscriptionByProduct(Product product) {
        return subscriptionRepository.findByProduct(product);
    }

    public Collection<Subscription> getSubscriptionByUserAndProduct(Userx user, Product product) {
        return subscriptionRepository.findByUserAndProduct(user, product);
    }

    public Subscription createSubscription(Userx user, Product product) {
        Collection<Subscription> subscriptions = getSubscriptionByUserAndProduct(user, product);

        if (subscriptions.isEmpty()) {
            Subscription subscription = new Subscription();
            subscription.setUser(user);
            subscription.setProduct(product);
            return subscriptionRepository.save(subscription);
        } else {
            return subscriptions.stream().findFirst().get();
        }
    }
}
