package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.model.Userx;

import java.util.Collection;
import java.util.Optional;

public interface SubscriptionRepository extends AbstractRepository<Subscription, Long> {

    Collection<Subscription> findByUser(Userx user);

    Collection<Subscription> findByProduct(Product product);

    Optional<Subscription> findByUserAndProduct(Userx user, Product product);
}
